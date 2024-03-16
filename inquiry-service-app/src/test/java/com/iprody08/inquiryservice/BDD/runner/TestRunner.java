package com.iprody08.inquiryservice.BDD.runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions (
        glue = {"steps"})
public class TestRunner {

}
