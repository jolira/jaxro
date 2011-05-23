/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.google.code.joliratools.bind.apt;

//import static com.google.code.joliratools.bind.generate.XMLAdapterClassGenerator.ADAPTER_POSTFIX;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.java.dev.hickory.testing.Compilation;
import net.java.dev.hickory.testing.MemSourceFileObject;

import org.json.JSONTokener;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import com.google.code.joliratools.bind.schema.ArrayEntity;
import com.google.code.joliratools.bind.schema.CollectionEntity;

/**
 * Test the {{@link JAXROProcessor}.
 * 
 * @author Joachim Kainz
 */
public class JAXROProcessorJSONTest {
    private static final String ACCOUNT_NAME = "com.google.code.joliratools.bind.demo.Account";

    private static final String XS = "http://www.w3.org/2001/XMLSchema";

    private static final String CUSTOMER_NAME = "com.google.code.joliratools.bind.demo.Customer";
    private static final String FAKECUSTOMER_NAME = "com.google.code.joliratools.bind.demo.FakeCustomer";
    private static final String EXECUTOR_NAME = "com.google.code.joliratools.bind.demo.JSONExecutor";
    public final static String ADAPTER_POSTFIX = "JSONAdapter";

    private static void assertAccount(final XMLNode account) {
        assertEquals("Account", account.getAttribute("name"));
        assertEquals(XS, account.getNamespaceURI());
        assertEquals("complexType", account.getName());

        final XMLNode[] all = account.getChildren();

        assertEquals(1, all.length);

        assertEquals(XS, all[0].getNamespaceURI());
        assertEquals("all", all[0].getName());

        final XMLNode[] elements = all[0].getChildren();

        assertEquals(6, elements.length);

        for (final XMLNode element : elements) {
            assertEquals("element", element.getName());
            assertEquals(XS, element.getNamespaceURI());
        }

        assertEquals("notices", elements[0].getAttribute("name"));
        assertEquals("StringCollection", elements[0].getAttribute("type"));
        assertEquals("nicknames", elements[1].getAttribute("name"));
        assertEquals("StringCollection", elements[1].getAttribute("type"));
        assertEquals("accountHolders", elements[2].getAttribute("name"));
        assertEquals("CustomerCollection", elements[2].getAttribute("type"));
        assertEquals("balance", elements[3].getAttribute("name"));
        assertEquals("xs:double", elements[3].getAttribute("type"));
        assertEquals("number", elements[4].getAttribute("name"));
        assertEquals("xs:string", elements[4].getAttribute("type"));
        assertEquals("status", elements[5].getAttribute("name"));
        assertEquals("AccountStatus", elements[5].getAttribute("type"));
    }

    private static void assertAccountArray(final XMLNode array) {
        assertEquals("AccountArray", array.getAttribute("name"));
        assertEquals(XS, array.getNamespaceURI());
        assertEquals("complexType", array.getName());

        final XMLNode[] all = array.getChildren();

        assertEquals(1, all.length);

        assertEquals(XS, all[0].getNamespaceURI());
        assertEquals("sequence", all[0].getName());

        final XMLNode[] elements = all[0].getChildren();

        assertEquals(1, elements.length);

        for (final XMLNode element : elements) {
            assertEquals("element", element.getName());
            assertEquals(XS, element.getNamespaceURI());
        }

        assertEquals("entry", elements[0].getAttribute("name"));
        assertEquals("Account", elements[0].getAttribute("type"));
        assertEquals("unbounded", elements[0].getAttribute("maxOccurs"));
    }

    private static void assertAccountStatus(final XMLNode status) {
        assertEquals("AccountStatus", status.getAttribute("name"));
        assertEquals(XS, status.getNamespaceURI());
        assertEquals("simpleType", status.getName());

        final XMLNode[] restriction = status.getChildren();

        assertEquals(1, restriction.length);

        assertEquals(XS, restriction[0].getNamespaceURI());
        assertEquals("restriction", restriction[0].getName());
        assertEquals("xs:string", restriction[0].getAttribute("base"));

        final XMLNode[] elements = restriction[0].getChildren();

        assertEquals(2, elements.length);
        assertEquals("enumeration", elements[0].getName());
        assertEquals(XS, elements[0].getNamespaceURI());
        assertEquals("OPEN", elements[0].getAttribute("value"));
        assertEquals("enumeration", elements[1].getName());
        assertEquals(XS, elements[1].getNamespaceURI());
        assertEquals("CLOSED", elements[1].getAttribute("value"));
    }

    private static void assertCustomer(final XMLNode customer) {
        assertEquals("Customer", customer.getAttribute("name"));
        assertEquals(XS, customer.getNamespaceURI());
        assertEquals("complexType", customer.getName());

        final XMLNode[] all = customer.getChildren();

        assertEquals(1, all.length);

        assertEquals(XS, all[0].getNamespaceURI());
        assertEquals("all", all[0].getName());

        final XMLNode[] elements = all[0].getChildren();

        assertEquals(4, elements.length);

        for (final XMLNode element : elements) {
            assertEquals("element", element.getName());
            assertEquals(XS, element.getNamespaceURI());
        }

        assertEquals("accounts", elements[0].getAttribute("name"));
        assertEquals("AccountArray", elements[0].getAttribute("type"));
        assertEquals("name", elements[1].getAttribute("name"));
        assertEquals("xs:string", elements[1].getAttribute("type"));
        assertEquals("happy", elements[2].getAttribute("name"));
        assertEquals("xs:boolean", elements[2].getAttribute("type"));
        assertEquals("dateOfBirth", elements[3].getAttribute("name"));
        assertEquals("xs:dateTime", elements[3].getAttribute("type"));
    }

    private static void assertCustomerArrayElement(final XMLNode array) {
        assertEquals("customer" + ArrayEntity.POST_FIX, array
                .getAttribute("name"));
        assertEquals(XS, array.getNamespaceURI());
        assertEquals("element", array.getName());

        final XMLNode[] children = array.getChildren();

        assertEquals(0, children.length);
    }

    private static void assertEverythingExeptSchema(
            final Compilation compilation, final String schemaDocument)
            throws IOException, ClassNotFoundException, NoSuchMethodException,
            IllegalAccessException, InvocationTargetException,
            ParserConfigurationException, SAXException {
        if (schemaDocument == null || schemaDocument.length() < 10) {
            fail("generation failed:\n" + schemaDocument);
        }

        System.out.println(schemaDocument);
        final String genCustAdapter = compilation
                .getGeneratedSource(CUSTOMER_NAME + ADAPTER_POSTFIX);

        System.out.println(genCustAdapter);

        final String genAccontArrayAdapter = compilation
                .getGeneratedSource(ACCOUNT_NAME + ArrayEntity.POST_FIX
                        + ADAPTER_POSTFIX);

        System.out.println(genAccontArrayAdapter);

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = compilation
                .getDiagnostics();

        assertEquals(diagnostics.toString(), 2, diagnostics.size());

        final String genStringCollectionAdapter = compilation
                .getGeneratedSource("java.lang.String"
                        + CollectionEntity.POST_FIX + ADAPTER_POSTFIX);

        System.out.println(genStringCollectionAdapter);

        final String genCustomerArrayAdapter = compilation
                .getGeneratedSource(CUSTOMER_NAME + ArrayEntity.POST_FIX
                        + ADAPTER_POSTFIX);

        System.out.println(genCustomerArrayAdapter);

        final Compilation _compilation = new Compilation(compilation);

        load(EXECUTOR_NAME, _compilation);

        _compilation.doCompile(new PrintWriter(System.out), "-proc:none");

        final List<Diagnostic<? extends JavaFileObject>> _diagnostics = _compilation
                .getDiagnostics();

        assertEquals(_diagnostics.toString(), 0, _diagnostics.size());

        final Class<?> executorCls = _compilation.getOutputClass(EXECUTOR_NAME);
        final Method executeMethod = executorCls.getMethod("execute");

        final String instanceDocument = (String) executeMethod.invoke(null);

        System.out.println(instanceDocument);
        JSONTokener tokener = new JSONTokener(instanceDocument);
        assertNotNull(tokener);
        // assertSchemaInstance(instanceDocument);
    }

    private static void assertExists(final File dir, final String name)
            throws IOException {
        final String _name = name.replace('.', '/') + ADAPTER_POSTFIX + ".java";
        final File file = new File(dir, _name);
        final String content = readContent(file);

        assertNotNull(content);

        if (content.length() < 10) {
            fail("not a valid class: " + content);
        }
    }

    private static void assertResult(final Compilation compilation,
            final String schemaDocument) throws IllegalArgumentException,
            Exception {
        assertEverythingExeptSchema(compilation, schemaDocument);
        assertSchema(schemaDocument);
    }

    private static void assertSchema(final String schemaDocument)
            throws ParserConfigurationException, SAXException, IOException {
        final XMLNode schema = parse(schemaDocument);

        assertEquals(XS, schema.getNamespaceURI());
        assertEquals("schema", schema.getName());

        final XMLNode[] types = schema.getChildren();

        assertEquals(9, types.length);

        assertAccount(types[0]);
        assertAccountArray(types[1]);
        assertAccountStatus(types[2]);
        assertCustomer(types[3]);
        assertCustomerArrayElement(types[8]);
    }

    private static void assertSchemaInstance(final String instanceDocument)
            throws ParserConfigurationException, SAXException, IOException {
        final XMLNode instance = parse(instanceDocument);

        assertNotNull(instance);
    }

    private static void load(final String classname,
            final Compilation compilation) throws IOException {
        final String filename = "/" + classname.replace('.', '/') + ".java";
        final InputStream in = JAXROProcessorJSONTest.class
                .getResourceAsStream(filename);

        assertNotNull("test data missing: " + filename, in);

        final BufferedReader reader = new BufferedReader(new InputStreamReader(
                in));
        final MemSourceFileObject source = compilation.addSource(classname);

        try {
            for (;;) {
                final String line = reader.readLine();

                if (line == null) {
                    return;
                }

                source.addLine(line);
            }
        } finally {
            reader.close();
        }
    }

    /**
     * @param xmlContent
     * @return
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    private static XMLNode parse(final String xmlContent)
            throws ParserConfigurationException, SAXException, IOException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory
                .newInstance();

        factory.setNamespaceAware(true);

        final DocumentBuilder builder = factory.newDocumentBuilder();

        builder.setErrorHandler(new ErrorHandler() {
            @Override
            public void error(final SAXParseException exception)
                    throws SAXException {
                throw exception;
            }

            @Override
            public void fatalError(final SAXParseException exception)
                    throws SAXException {
                throw exception;
            }

            @Override
            public void warning(final SAXParseException exception)
                    throws SAXException {
                throw exception;
            }
        });

        final Document document = builder.parse(new InputSource(
                new StringReader(xmlContent)));
        final XMLNode root = new XMLNode(document);
        return root;
    }

    private static String readContent(final File file) throws IOException {
        final StringBuilder buf = new StringBuilder();
        final FileReader fileReader = new FileReader(file);
        final BufferedReader in = new BufferedReader(fileReader);

        try {
            for (;;) {
                final String line = in.readLine();

                if (line == null) {
                    break;
                }

                buf.append(line);
            }
        } finally {
            in.close();
        }

        return buf.toString();
    }

    private Compilation createCompilation() throws IOException {
        final Compilation compilation = new Compilation();

        load(CUSTOMER_NAME, compilation);
        load(ACCOUNT_NAME, compilation);
        load("com.google.code.joliratools.bind.demo.AccountStatus", compilation);
        load(FAKECUSTOMER_NAME, compilation);

        compilation.useProcessor(new JAXROProcessor());

        return compilation;
    }

    /**
     * Test the process without -A parameters.
     * 
     * @throws Exception
     *             something failed
     */
    // @Test
    // public void testProcess() throws Exception {
    // final Compilation compilation = createCompilation();
    //
    // compilation.doCompile(new PrintWriter(System.out)); // ,
    //
    // final String gen = compilation
    // .getGeneratedResource("com/google/code/joliratools/bind/demo/jaxro.xsd");
    //
    // assertResult(compilation, gen);
    // }

    /**
     * Test the process with the -Adense=true parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test
    public void testProcessDense() throws Exception {
        final Compilation compilation = createCompilation();
        final String option = "-Adense=true";

        compilation.doCompile(new PrintWriter(System.out), option);

        final String schemaDocument = compilation
                .getGeneratedResource("com/google/code/joliratools/bind/demo/jaxro.xsd");

        assertEverythingExeptSchema(compilation, schemaDocument);

        final XMLNode schema = parse(schemaDocument);

        assertEquals(XS, schema.getNamespaceURI());
        assertEquals("schema", schema.getName());

        final XMLNode[] types = schema.getChildren();

        // assertEquals(8, types.length);

        final XMLNode account = types[0];

        assertEquals("Account", account.getAttribute("name"));
        assertEquals(XS, account.getNamespaceURI());
        assertEquals("complexType", account.getName());

        final XMLNode[] all = account.getChildren();

        assertEquals(1, all.length);

        assertEquals(XS, all[0].getNamespaceURI());
        assertEquals("all", all[0].getName());

        final XMLNode[] elements = all[0].getChildren();

        // assertEquals(5, elements.length);

        for (final XMLNode element : elements) {
            assertEquals("element", element.getName());
            assertEquals(XS, element.getNamespaceURI());
        }

        // assertEquals("notices", elements[0].getAttribute("name"));
        // assertEquals("StringCollection", elements[0].getAttribute("type"));
        // assertEquals("nicknames", elements[1].getAttribute("name"));
        // assertEquals("StringCollection", elements[1].getAttribute("type"));
        // assertEquals("accountHolders", elements[2].getAttribute("name"));
        // assertEquals("CustomerCollection", elements[2].getAttribute("type"));
        // assertEquals("balance", elements[3].getAttribute("name"));
        // assertEquals("xs:double", elements[3].getAttribute("type"));
        // assertEquals("number", elements[4].getAttribute("name"));
        // assertEquals("xs:string", elements[4].getAttribute("type"));

        // assertAccountArray(types[1]);
        // assertCustomer(types[2]);
        // assertCustomerArrayElement(types[7]);
    }

    /**
     * Test the process with the -Ajaxroproc= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test
    public void testProcessInvalidProc() throws Exception {
        final Compilation compilation = createCompilation();

        compilation.doCompile(new PrintWriter(System.out), "-Ajaxroproc=jfk");

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = compilation
                .getDiagnostics();

        assertEquals(diagnostics.toString(), 2, diagnostics.size());
    }

    /**
     * Test the process with the -Ajaxroproc= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test(expected = RuntimeException.class)
    public void testProcessProcAdaptersOnly() throws Exception {
        final Compilation compilation = createCompilation();

        compilation.doCompile(new PrintWriter(System.out),
                "-Ajaxroproc=adaptersonly");

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = compilation
                .getDiagnostics();

        assertEquals(diagnostics.toString(), 2, diagnostics.size());

        compilation
                .getGeneratedResource("com/google/code/joliratools/bind/demo/jaxro.xsd");
    }

    /**
     * Test the process with the -Ajaxroproc= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test
    public void testProcessProcNone() throws Exception {
        final Compilation compilation = createCompilation();

        compilation.doCompile(new PrintWriter(System.out), "-Ajaxroproc=none");

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = compilation
                .getDiagnostics();

        assertEquals(diagnostics.toString(), 1, diagnostics.size());
    }

    /**
     * Test the process with the -Ajaxroproc= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test
    public void testProcessProcSchemaOnly() throws Exception {
        final Compilation compilation = createCompilation();

        compilation.doCompile(new PrintWriter(System.out),
                "-Ajaxroproc=schemaonly");

        final List<Diagnostic<? extends JavaFileObject>> diagnostics = compilation
                .getDiagnostics();

        assertEquals(diagnostics.toString(), 2, diagnostics.size());

        final String schemaDocument = compilation
                .getGeneratedResource("com/google/code/joliratools/bind/demo/jaxro.xsd");

        if (schemaDocument == null || schemaDocument.length() < 100) {
            fail("no schema document");
        }
    }

    /**
     * Test the process with the -Aadapters= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    @Test
    public void testProcessWithAdapterDir() throws Exception {
        final Compilation compilation = createCompilation();
        final File tmpDir = File.createTempFile("gen-", "-adapters", null);

        final boolean deleted = tmpDir.delete();

        assertTrue(deleted); // making everyone happy

        final String option = "-Aadapters=" + tmpDir.getAbsolutePath();

        compilation.doCompile(new PrintWriter(System.out), option);

        assertExists(tmpDir, ACCOUNT_NAME);
        assertExists(tmpDir, CUSTOMER_NAME);
    }

    /**
     * Test the process with the -Aschema= parameters.
     * 
     * @throws Exception
     *             something failed
     */
    // @Test
    // public void testProcessWithSchema() throws Exception {
    // final Compilation compilation = createCompilation();
    // final File tmpFile = File.createTempFile("gen-", "*.xsd", null);
    // final String option = "-Aschema=" + tmpFile.getAbsolutePath();
    //
    // compilation.doCompile(new PrintWriter(System.out), option);
    //
    // final String gen = readContent(tmpFile);
    //
    // assertResult(compilation, gen);
    // }
}
