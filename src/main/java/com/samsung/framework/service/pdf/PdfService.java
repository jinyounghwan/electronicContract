package com.samsung.framework.service.pdf;


import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorker;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.itextpdf.tool.xml.css.CssFile;
import com.itextpdf.tool.xml.css.StyleAttrCSSResolver;
import com.itextpdf.tool.xml.html.CssAppliers;
import com.itextpdf.tool.xml.html.CssAppliersImpl;
import com.itextpdf.tool.xml.html.Tags;
import com.itextpdf.tool.xml.parser.XMLParser;
import com.itextpdf.tool.xml.pipeline.css.CSSResolver;
import com.itextpdf.tool.xml.pipeline.css.CssResolverPipeline;
import com.itextpdf.tool.xml.pipeline.end.PdfWriterPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.samsung.framework.common.utils.DateUtil;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
public class PdfService {
    private final String PDF_STORAGE_PATH= File.separator+ "Contract" + File.separator + "PDF" +File.separator;

    @Value("${properties.file.rootDir}")
    private String getRootDir;
    @Value("${properties.file.realDir}")
    private String getRealDir;
    @Value("${server.port}")
    private String port;
    @Value("${ip.local-address}")
    private String localAddress;

    public FilePublicVO createPDF(String html, HttpServletRequest request) throws Exception {

        // html \n 문자 -> 빈칸으로 변경
        html = this.htmlTagConvert(html);
        String serverIp = this.getPdfAddressImgUrl(request);

        String convertHtml = FileUtil.imgTagSetting(html,serverIp);
        String createFileName = FileUtil.createPdfFileName();
        String nowDay = DateUtil.getUtcNowDateFormat("yyMM");
        // 파일 저장 위치 설정
        final String storagePath = FileUtil.getOsRootDir() + getRootDir + getRealDir + PDF_STORAGE_PATH + nowDay;
        // 최초 PDF 저장 시 PDF 폴더가 없다면 생성
        FileUtil.makeDirectories(storagePath);

        log.info("convertHtml :: {} ", convertHtml);

        // 실제 저장위치 및 파일이름
        final String paths = storagePath + File.separator + createFileName;

        log.info("File Paths : {} ", paths);

        // 최초 문서 사이즈 설정
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        try {
            // PDF 파일 생성
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(paths));
            // PDF 파일에 사용할 폰트 크기 설정
            pdfWriter.setInitialLeading(12.5f);
            // PDF 파일 열기
            document.open();

            // CSS 설정 변수 세팅
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssFile commonCssFile = null;
            CssFile fontCssFile = null;
            try {
                InputStream cssStream = getClass().getClassLoader().getResourceAsStream("static/css/pdf.css");
                commonCssFile = XMLWorkerHelper.getCSS(cssStream);
                cssStream = getClass().getClassLoader().getResourceAsStream("static/css/font.css");
                fontCssFile = XMLWorkerHelper.getCSS(cssStream);
            } catch (Exception e) {
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }
            if (commonCssFile == null || fontCssFile == null) {
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }
            cssResolver.addCss(commonCssFile);
            cssResolver.addCss(fontCssFile);

            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

            try {
                fontProvider.register("static/font/AppleSDGothicNeoR.ttf", "AppleSDGothicNeo");
            } catch (Exception e) {
                throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
            }
            if (fontProvider.getRegisteredFonts() == null) {
                throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
            }

            // 사용할 폰트를 담아두었던 내용을 CSSAppliersImpl에 담아 적용
            CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);

            // HTML Pipeline 생성
            HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
            htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
            PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
            CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

            XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
            XMLParser xmlParser = new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);

            StringReader stringReader = new StringReader(convertHtml);
            xmlParser.parse(stringReader);
            document.close();
            pdfWriter.close();
        } catch (DocumentException e1) {
            throw new IllegalArgumentException("PDF 라이브러리 설정 에러");
        } catch (FileNotFoundException e2) {
            throw new IllegalArgumentException("PDF 파일 생성중 에러");
        } catch (IOException e3) {
            throw new IllegalArgumentException("PDF 파일 생성중 에러2");
        } catch (Exception e4) {
            throw new IllegalArgumentException("PDF 파일 생성중 에러3");
        } finally {
            try {
                document.close();
            } catch (Exception e) {
                log.info("PDF 파일 닫기 에러");
                e.printStackTrace();
            }
            String filePath = FileUtil.seperateOs(storagePath);
            filePath = filePath.substring(0,filePath.lastIndexOf(File.separator));
            log.info("filePath :: {}",filePath);

            return FilePublicVO.builder()
                    .fileNo(1)
                    .storagePath(FileUtil.seperateOs(storagePath))
                    .name(createFileName)
                    .extension(FileUtil.getFileExtension(createFileName))
                    .delYn("N")
                    .originalName(createFileName)
                    .size(String.valueOf(FileUtil.getFileSize(paths)))
                    .build();
        }
    }
    public String htmlTagConvert(String html){
        html = html.replaceAll("\n"," ");
        html = html.replaceAll("<br>", "<br/>");
        html = html.replaceAll("<strong>", "<p>");
        html = html.replaceAll("</strong>", "</p>");
        return html;
    }

    /**
     * serverIp에 따른 url
     * @param request
     * @return
     */
    public String getPdfAddressImgUrl(HttpServletRequest request){
        String serverIp = request.getRemoteAddr();
        String prefix;
        if(serverIp.equals(localAddress)){
            prefix = "http://localhost:"+port;
        } else {
            prefix = "http://"+serverIp+":"+port;
        }
        log.info("server pdfAddress ::  "+prefix);

        return prefix;
    }
}
