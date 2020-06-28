<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="utf-8"/>
    <xsl:template match="Students">
        <xsl:apply-templates/>
        <Students>
            <xsl:for-each select="student">
                <student>
                    <id>
                        <xsl:value-of select="学号"/>
                        <xsl:value-of select="学号"/>
                        <xsl:value-of select="Sno"/>
                    </id>
                    <name>
                        <xsl:value-of select="姓名"/>
                        <xsl:value-of select="名称"/>
                        <xsl:value-of select="Snm"/>
                    </name>
                    <sex>
                        <xsl:value-of select="性别"/>
                        <xsl:value-of select="性别"/>
                        <xsl:value-of select="Sex"/>
                    </sex>
                    <major>
                        <xsl:value-of select="院系"/>
                        <xsl:value-of select="专业"/>
                        <xsl:value-of select="Sdo"/>
                    </major>

                </student>
            </xsl:for-each>
        </Students>
    </xsl:template>
</xsl:stylesheet>
