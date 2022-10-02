/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.github.zlika.reproducible;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for {@link JaxbObjectFactoryFixer}.
 */
public class JaxbObjectFactoryFixerTest
{
    /**
     * Tests normalization of an ObjectFactory java file generated by xjc.
     * @throws IOException
     */
    @Test
    public void testObjectFactoryFixer() throws IOException
    {
        final File in = new File(this.getClass().getResource("ObjectFactory.java").getFile());
        final File out = File.createTempFile("ObjectFactory", null);
        out.deleteOnExit();

        final JaxbObjectFactoryFixer fixer = new JaxbObjectFactoryFixer(
                Collections.singletonList(StripJaxbMojo.XjcGenerator.COM_SUN_XML_BIND.getMatchingCommentText()),
                StandardCharsets.UTF_8);
        fixer.strip(in, out);

        final byte[] expected = Files.readAllBytes(new File(
                this.getClass().getResource("ObjectFactory-fixed.java").getFile()).toPath());
        final byte[] actual = Files.readAllBytes(out.toPath());
        Assert.assertArrayEquals(expected, actual);
        out.delete();
    }

    /**
     * Tests normalization of an ObjectFactory java file generated by the jvnet plugin using
     * eclipse implementation of JAXB.
     * @throws IOException
     */
    @Test
    public void testObjectFactoryEclipseFixer() throws IOException
    {
        final File in = new File(this.getClass().getResource("ObjectFactoryEclipse.java").getFile());
        final File out = File.createTempFile("ObjectFactoryEclipse", null);
        out.deleteOnExit();

        final JaxbObjectFactoryFixer fixer = new JaxbObjectFactoryFixer(
                Collections.singletonList(StripJaxbMojo.XjcGenerator.ORG_GLASSFISH_JAXB.getMatchingCommentText()),
                StandardCharsets.UTF_8);
        fixer.strip(in, out);

        final byte[] expected = Files.readAllBytes(new File(
                this.getClass().getResource("ObjectFactoryEclipse-fixed.java").getFile()).toPath());
        final byte[] actual = Files.readAllBytes(out.toPath());
        Assert.assertArrayEquals(expected, actual);
        out.delete();
    }

    /**
     * Tests that if the input file was not generated by xjc,
     * it is copied as-is into the output file.
     * @throws IOException
     */
    @Test
    public void testWrongObjectFactoryFile() throws IOException
    {
        final File in = new File(this.getClass().getResource("WrongObjectFactory.java").getFile());
        final File out = File.createTempFile("WrongObjectFactory", null);
        out.deleteOnExit();

        final JaxbObjectFactoryFixer fixer = new JaxbObjectFactoryFixer(
                Collections.singletonList(StripJaxbMojo.XjcGenerator.COM_SUN_XML_BIND.getMatchingCommentText()),
                StandardCharsets.UTF_8);
        fixer.strip(in, out);

        final byte[] expected = Files.readAllBytes(new File(
                this.getClass().getResource("WrongObjectFactory.java").getFile()).toPath());
        final byte[] actual = Files.readAllBytes(out.toPath());
        Assert.assertArrayEquals(expected, actual);
        out.delete();
    }
}
