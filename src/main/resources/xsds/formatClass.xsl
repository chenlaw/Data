<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:template match="classes">
        <xsl:for-each select="class">
            <id>
                <xsl:value-of select="Cno"/>
            </id>
            <name>
                <xsl:value-of select="Cna"/>
            </name>
            <score>
                <xsl:value-of select="Cpt"/>
            </score>
            <teacher>
                <xsl:value-of select="Tec"/>
            </teacher>
            <location>
                <xsl:value-of select="Loc"/>
            </location>
        </xsl:for-each>
    </xsl:template>

</xsl:stylesheet>