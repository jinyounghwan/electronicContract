//package com.samsung.framework.service.mail;
//
//import com.samsung.framework.common.utils.DateUtil;
//import com.samsung.framework.common.utils.GenType;
//import com.samsung.framework.common.utils.StringUtil;
//import com.samsung.framework.service.common.ParentService;
//import com.samsung.framework.vo.member.MemberVO;
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Slf4j
//@Service
//@RequiredArgsConstructor
//public class EmailPublicServiceImpl extends ParentService implements MailService{
//
//    @Value("${spring.mail.username}")
//    private String senderEmail;
//
//    private final JavaMailSender javaMailSender;
//
//
//    /**
//     * 이메일 인증을 위한 메시지 생성
//     * @param member
//     * @return
//     */
//    @Override
//    public void mailAuthSend(MemberVO member) {
//        int emailAuthNumber = createEmailAuthNumber();
//        MimeMessage message = javaMailSender.createMimeMessage();
//
//        messageSetting(member, message, emailAuthNumber);
//        MemberVO target =  handleMailObject(member, emailAuthNumber);
//
//        log.info("mailService Info : {}", target);
//        getCommonMapper().getMailMapper().insertMailAuth(target); //Mail 인증정보를 넣어준다.
//        javaMailSender.send(message);
//
//    }
//
//    @Override
//    public MemberVO getEmail(MemberVO member) {
//        return getCommonMapper().getMailMapper().getAuthEmailMember(member);
//    }
//
//    @Override
//    public Map<String,Object> getAuthNumber(MemberVO member) throws Exception {
//        MemberVO target = settingDateObject(member);
//        MemberVO memberInfo = getCommonMapper().getMailMapper().getAuthNumber(target);
//        HashMap<String, Object> result= new HashMap<>();
//
//        if(StringUtil.isEmpty(memberInfo)){
//                result.put("message","입력하신 인증번호가 일치하지 않습니다.");
//        }else{
//                result.put("memberInfo",memberInfo);
//                result.put("message", "success");
//        }
//        return result;
//    }
//
//
//    /**
//     * 날짜 데이터 MemberVO authEmailSendTime에 Setting
//     * @param member
//     * @return
//     * @throws Exception
//     */
////    public MemberVO settingDateObject(MemberVO member) throws Exception {
////        String time = DateUtil.secondToMinuteFormatSetting(member.getAuthEmailSendTime()); // 초를 분초 형태로 변경
////        String currentDate = DateUtil.getUtcNowDateFormat("yyyyMMddHHmmss");
////        String sendTimeGap = DateUtil.getUtcNowDateFormat("yyyyMMddHH") + time;
////        String diffTime = DateUtil.secondToMinuteFormatSetting(String.valueOf(DateUtil.secDiffDate(currentDate,sendTimeGap)));
////        log.info("diffTime : {}", diffTime);
////        String sendTimeDate = DateUtil.getUtcNowDateFormat("yyyyMMddHH") + diffTime;
////
////        log.info("before Date : {} , SendTimeGap: {} , Send Date : {}", DateUtil.getUtcNowDateFormat("yyyyMMddHHmmss"), DateUtil.getUtcNowDateFormat("yyyyMMddHH"), DateUtil.getUtcNowDateFormat("yyyyMMddHH"));
////        log.info("Current Date : {} , SendTimeGap : {} , Send Date : {}", currentDate, sendTimeGap, sendTimeDate);
////        MemberVO target = MemberVO.builder()
////                                   .userEmail(member.getUserEmail())
////                                   .authEmailSendTime(sendTimeDate)
////                                   .userEtc(member.getUserEtc())
////                                   .build();
////
////        return target;
////    }
//
//
//    /**
//     * MailVO 객체 생성
//     * @param member
//     * @param emailAuthNumber
//     * @return
//     */
////    private MemberVO handleMailObject(MemberVO member, int emailAuthNumber) {
////        return  MemberVO.builder()
////                .userEmail(member.getUserEmail())
////                .userEtc(String.valueOf(emailAuthNumber)).build();
////    }
//
//    /**
//     * 이메일 인증 정보를 위한 랜덤 인증 번호를 생성
//     * @return
//     */
////    private int createEmailAuthNumber() {
////        int number = Integer.parseInt(getStringUtil().getRandomValue(GenType.Number, 6));
////        return number;
////    }
//
//    /**
//     * message를 보내기 위한 Setting
//     * @param member
//     * @param message
//     * @param number
//     */
////    private void messageSetting(MemberVO member, MimeMessage message, int number) {
////        try{
////            message.setFrom(senderEmail);
////            message.setRecipients(MimeMessage.RecipientType.TO, member.getUserEmail());
////            message.setSubject("이메일 인증");
////            String body = "";
////            body += "<h3>" + "요청하신 인증 번호 입니다." + "</h3>";
////            body += "<h1>" + number + "</h1>";
////            body += "<h3>" + "감사합니다." + "</h3>";
////            message.setText(body,"UTF-8", "html");
////        }catch (MessagingException e){
////            e.printStackTrace();
////        }
////    }
//}
