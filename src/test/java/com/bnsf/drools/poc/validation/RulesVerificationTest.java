package com.bnsf.drools.poc.validation;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.reflect.ClassPath;

import org.drools.core.io.impl.ClassPathResource;
import org.drools.verifier.Verifier;
import org.drools.verifier.VerifierError;
import org.drools.verifier.builder.VerifierBuilder;
import org.drools.verifier.builder.VerifierBuilderFactory;
import org.drools.verifier.data.VerifierReport;
import org.drools.verifier.report.VerifierReportWriter;
import org.drools.verifier.report.VerifierReportWriterFactory;
import org.drools.verifier.report.components.Severity;
import org.drools.verifier.report.components.VerifierMessageBase;
import org.junit.Assert;
import org.junit.Test;
import org.kie.api.io.ResourceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 *
 * Validates rules for Compilation errors and generates report listing any error,warning messages
 *
 * Created by rakesh on 9/17/15.
 */
public class RulesVerificationTest {
    private final transient Logger logger = LoggerFactory.getLogger(getClass());

    @Test
    public void validateRules() throws IOException {
        //get list of all the rules in the classpath
        Iterable<String> rules = getListOfRuleFiles();

        logger.info("Validating rules {}",rules);

        //add the rules to the verifier
        VerifierBuilder vBuilder = VerifierBuilderFactory.newVerifierBuilder();
        Verifier verifier = vBuilder.newVerifier();
        for (String rule : rules) {
            verifier.addResourcesToVerify( new ClassPathResource( rule, RulesVerificationTest.class ), ResourceType.DRL );
        }

        if(verifier.hasErrors()){
            List<VerifierError> errors = verifier.getErrors();
            for (VerifierError error : errors) {
                logger.error(error.getMessage());
            }
            Assert.fail("Rule has compiler errors");
        }

        verifier.fireAnalysis();

        //printMessages(verifier, Severity.ERROR);
        //printMessages(verifier, Severity.WARNING);
        //printMessages(verifier, Severity.NOTE);

        writeReport(verifier);
    }

    private void writeReport(Verifier verifier) throws IOException {
        VerifierReportWriter reportWriter = VerifierReportWriterFactory.newHTMLReportWriter();
        FileOutputStream fos = new FileOutputStream("target/htmlReport.zip");
        reportWriter.writeReport(fos, verifier.getResult());
    }

    public void printMessages(Verifier verifier, Severity severity){
        VerifierReport report = verifier.getResult();
        for(VerifierMessageBase base : report.getBySeverity( severity ) ){
            System.out.println( base );
        }
    }

    public Iterable<String> getListOfRuleFiles() throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        ImmutableSet<ClassPath.ResourceInfo> resourceInfos = classPath.getResources();


        Iterable<ClassPath.ResourceInfo> classpathResources = Iterables.filter(resourceInfos, new Predicate<ClassPath.ResourceInfo>() {
            public boolean apply(ClassPath.ResourceInfo input) {
                return input.getResourceName().endsWith(".drl")
                        && !input.getResourceName().startsWith("org/drools");
            }
        });

        Function<ClassPath.ResourceInfo, String> function = new Function<ClassPath.ResourceInfo, String>() {
            public String apply(ClassPath.ResourceInfo input) {
                return input.getResourceName();
            }
        };

        Iterable<String> result = Iterables.transform(classpathResources, function);
        return result;
    }
}
