package com.trgr.dockets.core.util;

import junit.framework.Assert;

import org.junit.Test;

import com.trgr.dockets.core.util.NyNormalizationUtility;


public class NyNormalizationUtilityTest 
{
                
                @Test
                public void testSRLDashPrefix1()
                {
                                String name = "SRL - WALKER, FREDERICK V.";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                 System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be FREDERICK V. WALKER, SRL  but is " +  actualname, "FREDERICK V. WALKER, SRL".equals(actualname));
                }
                @Test
                public void testSRLSpacePostfix2()
                {
                                String name = "SHEILA BIRNER   SRL";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be SHEILA BIRNER, SRL but is " +  actualname, "SHEILA BIRNER, SRL".equals(actualname));
                }
                @Test
                public void testSRLDashPostfix3()
                {
                                String name = "JONATHAN TURCOTTE - SRL";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be JONATHAN TURCOTTE, SRL but is " +  actualname, "JONATHAN TURCOTTE, SRL".equals(actualname));
                }
                
                @Test
                public void testSRLPrefix4()
                {
                                String name = "SRL FRANK SERAFINI";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be FRANK SERAFINI, SRL but is " +  actualname, "FRANK SERAFINI, SRL".equals(actualname));
                }
                @Test
                public void testOutPrefix5()
                {
                                String name = "RICHARD STOLOFF - OUT";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be RICHARD STOLOFF but is " +  actualname, "RICHARD STOLOFF".equals(actualname));
                }
               
                @Test
                public void testJudgeName1()
                {
                                String name = "PART 25 - JAMES W. HUTCHERSON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be JAMES W. HUTCHERSON but is: "+actualName,"JAMES W. HUTCHERSON".equals(actualName));
                }
                
                @Test
                public void testJudgeName2()
                {
                                String name = "PT 20 - JAMES W HUTCHERSON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be JAMES W HUTCHERSON but is: "+actualName,"JAMES W HUTCHERSON".equals(actualName));
                }
                
                @Test
                public void testJudgeName3()
                {
                                String name = "PART-7 JOSEPH F. BRUNO";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be JOSEPH F. BRUNO but is: "+actualName,"JOSEPH F. BRUNO".equals(actualName));
                }
                
                @Test
                public void testJudgeName4()
                {
                                String name = "PART 20, LAURA LEE JACOBSON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be LAURA LEE JACOBSON but is: "+actualName,"LAURA LEE JACOBSON".equals(actualName));
                }
                
                @Test
                public void testJudgeName5()
                {
                                String name = "PART23, MICHAEL GARSON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be MICHAEL GARSON but is: "+actualName,"MICHAEL GARSON".equals(actualName));
                }
                
                @Test
                public void testJudgeName6()
                {
                                String name = "PT. 22, MARTIN M. SOLOMON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be MARTIN M. SOLOMON but is: "+actualName,"MARTIN M. SOLOMON".equals(actualName));
                }
                
                @Test
                public void testJudgeName7()
                {
                                String name = "BERNARD J. GRAHAM, PT 36";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be BERNARD J. GRAHAM but is: "+actualName,"BERNARD J. GRAHAM".equals(actualName));
                }
                
                @Test
                public void testJudgeName8()
                {
                                String name = "PT. 22, MARTIN M. SOLOMON";
                                String actualName = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualName);
                                Assert.assertTrue("Name should be MARTIN M. SOLOMON but is: "+actualName,"MARTIN M. SOLOMON".equals(actualName));
                }
               /* @Test
                public void testJudgeName9()
                {
                
                                String name = "HOWLANDS LAKE PARTNERS";
                                boolean retVal = NyNormalizationUtility.isFirm(name);
                                Assert.assertTrue(name + " was not supposed to be a firm.", retVal);
                                System.out.println(name  + " is a firm.");
                
                }*/
                @Test
                public void testESQOutPrefix10()
                {
                                String name = "ESQ. -OUT LIONEL ETRA";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be LIONEL ETRA, ESQ. but is " +  actualname, "LIONEL ETRA, ESQ.".equals(actualname));
                }
                
                @Test
                public void testESQPeriodPrefix8()
                {
                                String name = "ESQ. OUT MATTHEW WHRITENOUR";
                                String actualname = NyNormalizationUtility.normalizeNameWithTitleNY(name);
                                System.out.println(name + " should be " + actualname);
                                Assert.assertTrue("Name should be MATTHEW WHRITENOUR, ESQ. but is " +  actualname, "MATTHEW WHRITENOUR, ESQ.".equals(actualname));
                }
}
