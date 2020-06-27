<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="gb2312"/>
    <xsl:template match="Students">
        <xsl:apply-templates/>
        <Students>
            <xsl:for-each select="student">
                <student>
                    <学号>
                        <xsl:value-of select="id"/>
                    </学号>
                    <名称>
                        <xsl:value-of select="name"/>
                    </名称>
                    <性别>
                        <xsl:value-of select="sex"/>
                    </性别>
                    <专业>
                        <xsl:value-of select="major"/>
                    </专业>

                </student>
            </xsl:for-each>
        </Students>
    </xsl:template>
</xsl:stylesheet>
