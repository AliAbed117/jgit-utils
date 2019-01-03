package com.dono.foundation;

import java.io.File;

import org.eclipse.jgit.api.Git;

import com.dono.foundation.utils.JGitUtil;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class UtilsTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public UtilsTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( UtilsTest.class );
    }

    /**
     * TODO: Complete the update server info test.
     * Rigourous Test :-)
     * @throws Exception 
     */
    public void testUpdateServerInfo() throws Exception
    {
        Git git = Git.open(new File("COMPLETE-PATH"));
        JGitUtil.updateServerInfo(git);
    }
}
