package com.samsung.framework.service.authority;

import com.samsung.framework.domain.authority.Authority;
import com.samsung.framework.service.common.ParentService;
import com.samsung.framework.vo.code.CommonCodeVO;
import com.samsung.framework.vo.file.FilePublicVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthorityServiceImpl extends ParentService implements AuthorityService {
    @Override
    public List<CommonCodeVO> findMenuList(String deptCd) {
        List<CommonCodeVO> list = getCommonMapper().getMenuMapper().findMenuList(deptCd);
        return list;
    }

    @Override
    public List<CommonCodeVO> findDepartmentList() {
        List<CommonCodeVO> list = getCommonMapper().getAuthorityMapper().findDepartmentList();
        return list;
    }

    @Override
    public Map<String, Object> saveIndividualAuthority(List<Authority> tgt) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "권한이 저장 완료 되었습니다.");

        AtomicInteger saveCount = new AtomicInteger(0);

        if(tgt.get(0).getUserIdList().size() > 0) {
            tgt.forEach(row -> {
                row.getUserIdList().forEach(user -> {
                    row.setUserId(user);
                    if(isMenuAuthorityGranted(row)) {
                        saveCount.addAndGet(getCommonMapper().getAuthorityMapper().updateIndividualAuthority(row));
                    }else {
                        saveCount.addAndGet(getCommonMapper().getAuthorityMapper().saveIndividualAuthority(row));
                    }
                });
            });
        }else {
            result.put("code", 204);
            result.put("message", "회원을 선택 해주세요.");

            return result;
        }

        if(saveCount.get() < 1) {
            result.put("code", 204);
            result.put("message", "권한 등록에 실패했습니다.");
        }

        return result;
    }

    @Override
    public Map<String, Object> saveGroupAuthority(List<Authority> tgt) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("message", "권한이 저장 완료 되었습니다.");

        AtomicInteger saveCount = new AtomicInteger(0);

        if(tgt.size() > 0) {
            tgt.forEach(row -> {
                if(isMenuAuthorityGranted(row)) {
                    saveCount.addAndGet(getCommonMapper().getAuthorityMapper().updateIndividualAuthority(row));
                }else {
                    saveCount.addAndGet(getCommonMapper().getAuthorityMapper().saveIndividualAuthority(row));
                }
            });
        }else {
            result.put("code", 204);
            result.put("message", "회원을 선택 해주세요.");

            return result;
        }

        if(saveCount.get() < 1) {
            result.put("code", 204);
            result.put("message", "권한 등록에 실패했습니다.");
        }

        return result;
    }

    /**
     * 권한 부여 여부 확인
     * @param authority
     * @return
     */
    private boolean isMenuAuthorityGranted(Authority authority) {
        log.info(authority.toString());
        long menuSeq = getCommonMapper().getMenuMapper().findMenuSeqByUserId(authority);
        if(menuSeq < 1) return false;
        authority.setMenuSeq(menuSeq);

        return true;
    }

    /**
     * 엑셀 권한 변경
     * @param file
     */
    public Map<String,Object> updAuthFile(FilePublicVO file, int memberSeq) throws IOException {
        List<Authority> userMenuList =getCommonMapper().getAuthorityMapper().findUserMenuAuthority(memberSeq);

        ArrayList<String> list = (ArrayList<String>) getExcelUtil().readExcel(file.getStoragePath());
        ArrayList<String> menuOrgList = (ArrayList<String>) createMenuList(list);
        int count = 1;
        ArrayList<Authority> menuAuthList = new ArrayList<>();
        Authority authority = null;
        for(String menu : menuOrgList){
            if(count==1) authority = new Authority();

            if(menu.contains("MENU")){
                authority.setMenuCd(menu);
            }else if(menu.contains("노출")){
                authority.setDisplayUseYn(menu.substring(0,1));
            }else if(menu.contains("조회")){
                authority.setAuthR(menu.substring(0,1));
                authority.setAuthC(menu.substring(3,4));
                authority.setAuthU(menu.substring(6,7));
                authority.setAuthD(menu.substring(9,10));
                authority.setAuthF(menu.substring(12,13));
            }

            if(count==5){
                menuAuthList.add(authority);
                count = 0;
            }
            count++;
        }

        int rowAffected = 0;
        for(Authority auth : menuAuthList){
            rowAffected= getCommonMapper().getAuthorityMapper().updateExcelAuthority(auth);
            if(rowAffected < 0) break;
        }

        HashMap<String,Object> map = new HashMap<>();
        if(rowAffected < 0) {
            map.put("result", "fail");
        } else{
            map.put("result", "success");
        }

        return map;
    }

    /**
     * 엑셀 데이터에서 뽑아온 세부 메뉴 명 가져온다.
     * @param list
     * @return
     */
    private List<String> createMenuList(ArrayList<String> list) {
        int menuCount = 1;
        ArrayList<String> menuOrgNm = new ArrayList<>();

        for(int i = 0; i < list.size(); i++){
            Boolean flag = (menuCount)%3 == 0;

            if (menuCount == 5) menuCount = 0;

            if(flag){
                log.info(list.get(i));
                menuOrgNm.add(list.get(i));
            }
            menuCount ++;
        }
        List<CommonCodeVO> commonCodeVOList = getCommonMapper().getAuthorityMapper().findMenuAuthority(menuOrgNm);
        for(CommonCodeVO codeVO : commonCodeVOList){
            int count =0;
            for(String auth : list){
                if(auth.equals(codeVO.getCodeNm())){
                    list.set(count,codeVO.getCodeCd());
                }
                count++;
            }
        }

        return list;
    }
}
