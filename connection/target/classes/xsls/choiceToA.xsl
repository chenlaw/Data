<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="gb2312"/>
    <xsl:template match="choices">
        <xsl:apply-templates/>
        <Choices>
            <xsl:for-each select="choice">
                <choice>
                    <学号>
                        <xsl:value-of select="sid"/>
                    </学号>
                    <课程编号>
                        <xsl:value-of select="cid"/>
                    </课程编号>
                    <成绩>
                        <xsl:value-of select="score"/>
                    </成绩>
                </choice>
            </xsl:for-each>
        </Choices>
    </xsl:template>
</xsl:stylesheet>

