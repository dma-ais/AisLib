/* Copyright (c) 2011 Danish Maritime Authority.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package dk.dma.ais.sentence;

import org.junit.Assert;
import org.junit.Test;

import dk.dma.ais.binary.SixbitException;
import dk.dma.ais.message.AisMessageException;

public class DecodeStiresTest {

    private Vdm vdm = new Vdm();

    /**
     * Test that sentences with preceeding comment blocks can be read
     * 
     * @throws SentenceException
     * @throws AisMessageException
     * @throws SixbitException
     */
    @Test
    public void basicCbTest() throws Exception {
        cbTest("\\c:1277725284*54\\!ABVDM,1,1,,A,13QKjr001jR94`8RBlB4ESLd45JT,0*5A");
        cbTest("\\1G2:4717,c:1277725285*02\\!BSVDM,2,1,0,A,54S<RB02@guMK8AGN20Lht84j0PDhDp6222222167Pt:75dl0;QjBSk`8888,0*20",
                1);
        cbTest("\\2G2:4717*78\\!BSVDM,2,2,0,A,88888888882,2*3F");
        cbTest("\\c:1277725287*57\\!BSVDM,1,1,,A,138ell002aQN?RhPd:R8>VTd<@=s,0*27");
        cbTest("\\c:1277725287*57\\!BSVDM,1,1,,B,138kWr002=1A29jPCOjB3Q`b40SC,0*56");
        cbTest("\\c:1277725287*57\\!ABVDM,1,1,,B,39NS?3Q001R6S5hRHQnHr8Fl4000,0*36");
        cbTest("\\c:1277725291*50\\!BSVDM,1,1,,A,33n<BF5P01QDrMLO<lD72gvl4000,0*4C");
        cbTest("\\1G2:4730,c:1277725292*01\\!BSVDM,2,1,4,B,53:P1J02?DwoD88k:208h4<eV222222222222216E@?:>5cE0H2hC2CRCQlP,0*75",
                1);
        cbTest("\\2G2:4730*7D\\!BSVDM,2,2,4,B,A;4UDlj@H82,2*51");
        cbTest("\\c:1277725302*5B\\!ABVDM,1,1,,A,144N:`0P?w<tSF0l4Q@>4?wp4t26,0*72");
        cbTest("\\c:1277725303*5A\\!BSVDM,1,1,,A,13S=T:002@Q>78JOw<l9uWwD<5JT,0*32");
        cbTest("\\c:1277725319*51\\!BSVDM,1,1,,B,14RLkV0P@0QEFnlO9coG9k5j40Rp,0*4C");
        cbTest("\\c:1277725319*51\\!ABVDM,1,1,,B,33Kc`J0OicQj`rPRIWr4Skob40vQ,0*3C");
        cbTest("\\c:1277725319*51\\!ABVDM,1,1,,A,147aqd0P001j;uHR2h`1Q?wV0<1p,0*0D");
        cbTest("\\c:1277725320*5B\\!ABVDM,1,1,,B,144isg`P00R6`I4RH@Ver7sd40S>,0*79");
        cbTest("\\c:1277725320*5B\\!BSVDM,1,1,,A,13iO8r001q1JKLpP>ru9kWkV4@On,0*19");
        cbTest("\\c:1277725321*5A\\!BSVDM,1,1,,B,13AbU4001NQG<D`OG6d7sVEn4@Rf,0*2E");
        cbTest("\\c:1277725321*5A\\!ABVDM,1,1,,B,39NS93U000R6`K@RHaBd>`b24000,0*0E");
        cbTest("\\c:1277725323*58\\!ABVDM,1,1,,B,33mU2`100326QW`REfC<d`p44000,0*6E");
        cbTest("\\c:1277725324*5F\\!BSVDM,1,1,,A,13pr<U0P1@1EMTlO8GS;;?v04H0V,0*46");
        cbTest("\\c:1277725325*5E\\!ABVDM,1,1,,B,144alwPP@122b:TRbuIcSSL:6D0W,0*14");
        cbTest("\\c:1277725326*5D\\!ABVDM,1,1,,B,13m?0N30051ghtNR<U>9W7d040R`,0*3C");
        cbTest("\\c:1277725327*5C\\!ABVDM,1,1,,A,13KFiQg0001q3hHROFDKuVn805J`,0*10");
        cbTest("\\c:1277725312*5A\\!ABVDM,1,1,,A,144i7oPP002:Qc8RBCS=w?wQt5JT,0*23");
    }

    private void cbTest(String line, int expect) throws Exception {
        int result = vdm.parse(line);
        Assert.assertEquals("vdm parse failed", expect, result);
        if (result == 0) {
            vdm = new Vdm();
        }
    }

    private void cbTest(String line) throws Exception {
        cbTest(line, 0);
    }

}
