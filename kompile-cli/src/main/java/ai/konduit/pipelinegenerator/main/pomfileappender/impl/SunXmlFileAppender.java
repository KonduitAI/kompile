package ai.konduit.pipelinegenerator.main.pomfileappender.impl;

import ai.konduit.pipelinegenerator.main.pomfileappender.PomFileAppender;

import java.util.Arrays;
import java.util.List;

public class SunXmlFileAppender implements PomFileAppender {
    @Override
    public DependencyType dependencyType() {
        return DependencyType.SUN_XML;
    }

    @Override
    public List<String> classesToAppend() {
        return Arrays.asList(
                "com.sun.org.apache.xerces.internal.impl.dtd.XMLNSDTDValidator"
                ,"com.sun.org.apache.xerces.internal.impl.XMLEntityManager"
                ,"com.sun.org.apache.xerces.internal.impl.XMLEntityScanner"
                ,"com.sun.org.apache.xerces.internal.impl.XMLDocumentFragmentScannerImpl"
                ,"com.sun.org.apache.xerces.internal.impl.XMLScanner"
                ,"com.sun.org.apache.xerces.internal.util.FeatureState"
                ,"jdk.xml.internal.JdkXmlUtils"
                ,"com.sun.org.apache.xerces.internal.impl.XMLVersionDetector"
                ,"com.sun.org.apache.xerces.internal.xni.NamespaceContext"
                ,"com.sun.org.apache.xerces.internal.impl.XMLNSDocumentScannerImpl"
                ,"com.sun.xml.internal.stream.util.ThreadLocalBufferAllocator"
                ,"com.sun.org.apache.xerces.internal.util.XMLChar"
                ,"com.sun.org.apache.xerces.internal.impl.XMLEntityManager$EncodingInfo"
                ,"com.sun.org.apache.xerces.internal.impl.XMLDTDScannerImpl"
                ,"com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDProcessor",
                "com.sun.org.apache.xerces.internal.impl.dv.dtd.DTDDVFactoryImpl"
                ,"com.sun.org.apache.xerces.internal.impl.XMLDocumentScannerImpl"
                ,"com.sun.org.apache.xerces.internal.impl.dtd.XMLDTDValidator"
                ,"com.sun.org.apache.xerces.internal.impl.Constants"
                ,"jdk.xml.internal.SecuritySupport"
                ,"javax.xml.parsers.FactoryFinder"
                ,"com.sun.org.apache.xerces.internal.util.XMLSymbols"
                ,"com.sun.org.apache.xerces.internal.util.PropertyState"
                ,"jdk.xml.internal.JdkXmlUtils");

    }

    @Override
    public InitializeType initializeType() {
        return InitializeType.BUILD_TIME;
    }
}
