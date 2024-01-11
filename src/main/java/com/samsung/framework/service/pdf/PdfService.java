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
import com.itextpdf.tool.xml.pipeline.html.AbstractImageProvider;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipeline;
import com.itextpdf.tool.xml.pipeline.html.HtmlPipelineContext;
import com.itextpdf.tool.xml.pipeline.html.LinkProvider;
import com.samsung.framework.common.utils.FileUtil;
import com.samsung.framework.service.common.ParentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;


@Slf4j
@Service
public class PdfService extends ParentService{

    public static final String IMG_PATH = "/static/img";
    public void createPDF(String html) throws Exception {
        log.info("html : {}", html);
        // 최초 문서 사이즈 설정
        Document document = new Document(PageSize.A4, 30, 30, 30, 30);
        String paths = FileUtil.getOsRootDir() + getPropertiesUtil().getFile().getRootDir() + "/" +getPropertiesUtil().getFile().getRealDir()+ FileUtil.createPdfFileName();
        try{
            // PDF 파일 생성
            PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(paths));
            // PDF 파일에 사용할 폰트 크기 설정
            pdfWriter.setInitialLeading(12.5f);
            // PDF 파일 열기
            document.open();

            // CSS 설정 변수 세팅
            CSSResolver cssResolver =  new StyleAttrCSSResolver();
            CssFile cssFile = null;
            try{
                InputStream cssStream = getClass().getClassLoader().getResourceAsStream("static/css/pdf/ItextPdf.css");
                cssFile = XMLWorkerHelper.getCSS(cssStream);
            } catch(Exception e){
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }
            if(cssFile == null){
                throw new IllegalArgumentException("PDF CSS 파일을 찾을 수 없습니다.");
            }
            cssResolver.addCss(cssFile);

            XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);

            try{
                fontProvider.register("static/font/AppleSDGothicNeoR.ttf","AppleSDGothicNeo");
            } catch(Exception e) {
                throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
            }
            if(fontProvider.getRegisteredFonts() == null) {
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
            htmlPipelineContext.setImageProvider(new AbstractImageProvider() {
                @Override
                public String getImageRootPath() {
                    return IMG_PATH;
                }
            });
            htmlPipelineContext.setLinkProvider(new LinkProvider() {
                @Override
                public String getLinkRoot() {
                    return null ;
                }
            });
            XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
            XMLParser xmlParser = new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);
            //html
            //String testHtml = "<img sre";
            String html2 = "<img src='http://localhost:3030/img/sample.jpg'/>";
            StringReader stringReader = new StringReader(html);
            xmlParser.parse(stringReader);
            document.close();
            pdfWriter.close();
        } catch (DocumentException e1){
            throw new IllegalArgumentException("PDF 라이브러리 설정 에러");
        } catch(FileNotFoundException e2){
            throw new IllegalArgumentException("PDF 파일 생성중 에러");
        } catch(IOException e3){
            throw new IllegalArgumentException("PDF 파일 생성중 에러2");
        } catch(Exception e4){
            throw new IllegalArgumentException("PDF 파일 생성중 에러3");
        }
        finally {
            try{
                document.close();
            } catch(Exception e){
                log.info("PDF 파일 닫기 에러");
                e.printStackTrace();
            }
        }
    }

//    private static XMLParser getXmlParser(Document document, PdfWriter pdfWriter, CSSResolver cssResolver) {
//        HtmlPipelineContext htmlPipelineContext = getHtmlPipelineContext();
//
//        PdfWriterPipeline pdfWriterPipeline = new PdfWriterPipeline(document, pdfWriter);
//        HtmlPipeline htmlPipeline = new HtmlPipeline(htmlPipelineContext, pdfWriterPipeline);
//        CssResolverPipeline cssResolverPipeline = new CssResolverPipeline(cssResolver, htmlPipeline);
//
//        XMLWorker xmlWorker = new XMLWorker(cssResolverPipeline, true);
//        return new XMLParser(true, xmlWorker, StandardCharsets.UTF_8);
//    }
//
//    private static HtmlPipelineContext getHtmlPipelineContext() {
//        XMLWorkerFontProvider fontProvider = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
//
//        try{
//            fontProvider.register("static/font/AppleSDGothicNeoR.ttf","AppleSDGothicNeo");
//        } catch(Exception e) {
//            throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
//        }
//        if(fontProvider.getRegisteredFonts() == null) {
//            throw new IllegalArgumentException("PDF 폰트 파일을 찾을 수 없습니다.");
//        }
//
//        // 사용할 폰트를 담아두었던 내용을 CSSAppliersImpl에 담아 적용
//        CssAppliers cssAppliers = new CssAppliersImpl(fontProvider);
//
//        // HTML Pipeline 생성
//        HtmlPipelineContext htmlPipelineContext = new HtmlPipelineContext(cssAppliers);
//        htmlPipelineContext.setTagFactory(Tags.getHtmlTagProcessorFactory());
//        return htmlPipelineContext;
//    }

//    public void converterToPDF(String html) throws Exception  {
//        String paths = FileUtil.getOsRootDir() + getPropertiesUtil().getFile().getRootDir() + "/" +getPropertiesUtil().getFile().getRealDir()+ FileUtil.createPdfFileName();
//        PdfWriter writer = new PdfWriter(paths);
//        PdfDocument pdf = new PdfDocument(writer);
//        pdf.setTagged();
//        PageSize pageSize = new PageSize(500, 1500);
//        pdf.setDefaultPageSize(pageSize);
//
//        ConverterProperties properties = new ConverterProperties();
//        // 폰트 설정
//        FontProvider fontProvider = new DefaultFontProvider(false, false, false);
//        FontProgram fontProgram = FontProgramFactory.createFont("static/font/AppleSDGothicNeoR.ttf");
//        fontProvider.addFont(fontProgram);
//        properties.setFontProvider(fontProvider);
//        properties.setBaseUri("http://localhost:3030/img/");
//        // PDF화면 설정
//        MediaDeviceDescription mediaDeviceDescription = new MediaDeviceDescription(MediaType.SCREEN);
//        mediaDeviceDescription.setWidth(pageSize.getWidth());
//        properties.setMediaDeviceDescription(mediaDeviceDescription);
//
//        HtmlConverter.convertToPdf(html,new FileOutputStream(paths), properties);
//
//    }

}
