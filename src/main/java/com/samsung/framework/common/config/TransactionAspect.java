package com.samsung.framework.common.config;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.*;

import java.util.Collections;
import java.util.HashMap;

//@Configuration
public class TransactionAspect {

//    @Autowired
//    TransactionConfig transactionConfig;

//    @Bean
//    public TransactionInterceptor transactionAdvice() {
//
//        NameMatchTransactionAttributeSource txAttributeSource = new NameMatchTransactionAttributeSource();
//
//        RuleBasedTransactionAttribute readOnlyAttribute = new RuleBasedTransactionAttribute();
//        readOnlyAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//        readOnlyAttribute.setReadOnly(true);
//
//        RuleBasedTransactionAttribute writeOnlyAttribute = new RuleBasedTransactionAttribute();
//        writeOnlyAttribute.setRollbackRules(Collections.singletonList(new RollbackRuleAttribute(Exception.class)));
//        writeOnlyAttribute.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
//
//        HashMap<String, TransactionAttribute> txMethods = new HashMap<String, TransactionAttribute>();
//
//        // write전용 transaction (rollback처리)
////        txMethods.put("merge*", writeOnlyAttribute); //메뉴별 권한 체크를 위해 merge는 제외 처리
//        txMethods.put("insert*", writeOnlyAttribute);
//        txMethods.put("save*", writeOnlyAttribute);
//        txMethods.put("update*", writeOnlyAttribute);
//        txMethods.put("modify*", writeOnlyAttribute);
//        txMethods.put("delete*", writeOnlyAttribute);
//        txMethods.put("remove*", writeOnlyAttribute);
//
//
//        // readonly transaction (저장은 되나 rollback처리 안됨)
//        txMethods.put("paging*", readOnlyAttribute);
//        txMethods.put("list*", readOnlyAttribute);
//        txMethods.put("row*", readOnlyAttribute);
//        txMethods.put("get*", readOnlyAttribute);
//        txMethods.put("select*", readOnlyAttribute);
//        txMethods.put("one*", readOnlyAttribute);
//
//        txAttributeSource.setNameMap(txMethods);
//
//        return new TransactionInterceptor(transactionConfig.transactionManager(), txAttributeSource);
//    }

//    @Bean
//    public Advisor transactionAdviceAdvisor() {
//        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
//        pointcut.setExpression("execution(* com.samsung.framework.service..*Service.*(..))");
//        return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
//    }
}

