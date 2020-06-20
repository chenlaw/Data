<?xml version="1.0" encoding="gb2312" ?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="xml" encoding="gb2312"/>
    <xsl:template match="Classes">
        <xsl:apply-templates/>
        <Classes>
            <xsl:for-each select="class">
                <class>
                    <id>
                        <xsl:value-of select="课程编号"/>
                        <xsl:value-of select="编号"/>
                        <xsl:value-of select="cno"/>
                    </id>
                    <name>
                        <xsl:value-of select="名称"/>
                        <xsl:value-of select="课程名称"/>
                        <xsl:value-of select="cnm"/>
                    </name>
                    <time>0</time>
                    <score>
                        <xsl:value-of select="学分"/>
                        <xsl:value-of select="cpt"/>
                    </score>
                    <teacher>
                        <xsl:value-of select="老师"/>
                        <xsl:value-of select="授课老师"/>
                        <xsl:value-of select="Tec"/>
                    </teacher>
                    <location>
                        <xsl:value-of select="授课地点"/>
                        <xsl:value-of select="地点"/>
                        <xsl:value-of select="Pla"/>
                    </location>
                </class>
            </xsl:for-each>
        </Classes>
    </xsl:template>
</xsl:stylesheet>
