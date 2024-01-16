package com.samsung.framework.controller.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/mail")
public class MailController {

//    @PostMapping("/auth")
//    public ResponseEntity mailAuthSend(@RequestBody MemberVO member){
//       getCommonService().getMailService().mailAuthSend(member);
//       return new ResponseEntity(member, HttpStatus.OK);
//   }
//
//   @PostMapping("/getEmail")
//    public ResponseEntity emailCheck(@RequestBody MemberVO member){
//        MemberVO target = getCommonService().getMailService().getEmail(member);
//
//        return new ResponseEntity(target, HttpStatus.OK);
//   }
//
//
//    @PostMapping("/authNumberCheck")
//    public ResponseEntity authNumberCheck(@RequestBody MemberVO member) throws Exception {
//        Map<String,Object> target = getCommonService().getMailService().getAuthNumber(member);
//
//        return ResponseEntity.ok(target);
//    }


}
