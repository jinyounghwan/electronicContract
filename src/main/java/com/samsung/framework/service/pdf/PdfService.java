package com.samsung.framework.service.pdf;

import com.itextpdf.text.*;
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
import com.samsung.framework.mapper.contract.documented.ContractCreationMapper;
import com.samsung.framework.vo.file.FilePublicVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.itextpdf.text.Image;



@Slf4j
@RequiredArgsConstructor
@Service
public class PdfService {
    private final String PDF_STORAGE_PATH= File.separator+ "Contract" + File.separator + "PDF" +File.separator;
    private final ContractCreationMapper contractCreationMapper;

    @Value("${properties.file.rootDir}")
    private String getRootDir;
    @Value("${properties.file.realDir}")
    private String getRealDir;
    @Value("${server.port}")
    private String port;
    @Value("${ip.local-address}")
    private String localAddress;
    @Value("${ip.server-address}")
    private String serverAddress;



    public FilePublicVO createPDF(String html, HttpServletRequest request , String seq) throws Exception {
        // HTML에서 \n 문자를 빈칸으로 변경
        html = this.htmlTagConvert(html);

        log.info("html >> " + html);

        String createFileName = FileUtil.createPdfFileName();
        String nowDay = DateUtil.getUtcNowDateFormat("yyMM");

        // 파일 저장 경로 설정
        final String storagePath = FileUtil.getOsRootDir() + getRootDir + getRealDir + PDF_STORAGE_PATH + nowDay;
        FileUtil.makeDirectories(storagePath);

        // 실제 저장 경로 및 파일 이름
        final String paths = storagePath + File.separator + createFileName;

        log.info("paths >> " + paths);

        // PDF 문서 설정
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        try {
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(paths));
            pdfWriter.setInitialLeading(12.5f);
            document.open();

            // Base64 이미지를 처리한 후, 남은 HTML 파싱
            String modifiedHtml = processBase64Images(html, document); // HTML 내 Base64 이미지 처리

            // HTML에서 남은 내용 처리
            StringReader stringReader = new StringReader(modifiedHtml);

            // CSS 및 HTML 처리 파이프라인 설정
            CSSResolver cssResolver = new StyleAttrCSSResolver();
            CssAppliers cssAppliers = new CssAppliersImpl(new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS));

            HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
            htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());

            PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
            HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
            CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);

            XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
            XMLParser xmlParser = new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);

            xmlParser.parse(stringReader);  // HTML 파싱
            document.close();
            pdfWriter.close();

            FilePublicVO file = new FilePublicVO();
            Long longSeq = Long.parseLong(seq);
            int intSeq = Integer.parseInt(seq);
            file.setFileSeq(longSeq);
            file.setOriginalName(createFileName);
            file.setName(createFileName);
            file.setFileNo(intSeq);
            file.setExtension("pdf");
            file.setStoragePath(paths);
            file.setDelYn("N");
            file.setCreatedBy("admin"); // 사용자 아이디 들어가도록 변경해야함

            contractCreationMapper.saveFilePath(file);
        } catch (Exception e) {
            log.error("PDF 생성 중 오류 발생", e);
            throw new IllegalArgumentException("PDF 파일 생성 중 에러");
        }

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

    // Base64 이미지를 PDF에 추가하는 메서드
    private String processBase64Images(String html, Document document) throws DocumentException, IOException {
        Pattern pattern = Pattern.compile("<img src=\"data:image/(png|jpeg);base64,([^\"]+)\"[^>]*>");
        Matcher matcher = pattern.matcher(html);

        StringBuffer modifiedHtml = new StringBuffer();
        while (matcher.find()) {
            String base64Image = matcher.group(2);  // Base64 이미지 데이터 추출
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);  // Base64를 바이트 배열로 변환

            // iText를 사용하여 이미지 객체 생성
            Image image = Image.getInstance(imageBytes);
            image.scaleToFit(200, 100);  // 이미지 크기 조정

            // PDF 문서에 이미지 추가
            document.add(image);

            // HTML 내 <img> 태그를 빈 문자열로 대체
            matcher.appendReplacement(modifiedHtml, "");
        }

        matcher.appendTail(modifiedHtml);
        return modifiedHtml.toString();  // 이미지가 처리된 HTML 반환
    }

    public String htmlTagConvert(String html) {
        // 모든 <img> 태그가 닫히도록 보장
        html = html.replaceAll("<img([^>]*)(?<!/)>", "<img$1/>");

        html = html.replaceAll("\n", " ");
        html = html.replaceAll("<br>", "<br/>");

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
            prefix = "http://"+serverAddress+":"+port;
        }
        log.info("server pdfAddress ::  "+prefix);

        return prefix;
    }
}
