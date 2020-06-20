<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="gb2312"/>
    <xsl:template match="classes">
        <xsl:apply-templates/>
        <Classes>
            <xsl:for-each select="class">
                <class>
                    <cno>
                        <xsl:value-of select="id"/>
                    </cno>
                    <cnm><xsl:value-of select="name"/></cnm>
                    <cpt><xsl:value-of select="score"/></cpt>
                    <Tec><xsl:value-of select="teacher"/></Tec>
                    <Pla><xsl:value-of select="location"/></Pla>
                </class>
            </xsl:for-each>
        </Classes>
    </xsl:template>
</xsl:stylesheet>