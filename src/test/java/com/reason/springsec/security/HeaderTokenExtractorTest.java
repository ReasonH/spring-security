package com.reason.springsec.security;

import com.reason.springsec.security.jwt.HeaderTokenExtractor;
import com.reason.springsec.security.jwt.InvalidJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class HeaderTokenExtractorTest {
    private HeaderTokenExtractor extractor = new HeaderTokenExtractor();
    private String header;

    @BeforeEach
    public void setUp(){
        this.header = "Bearer dkjdkfajsdl.dsfjldasf.ldfajf";
    }

    @Test
    public void TEST_JWT_EXTRACT() throws InvalidJwtException {
        assertThat(extractor.extract(this.header), is("dkjdkfajsdl.dsfjldasf.ldfajf"));
    }

}
