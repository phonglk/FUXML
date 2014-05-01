<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="/">
        <xsl:for-each select="//Book">
            <a href="">
                <div class="search-result">
                    <xsl:element name="div">
                        <xsl:attribute name="data-id">
                            <xsl:value-of select="Id"/>
                        </xsl:attribute>
                    </xsl:element>
                    <xsl:element name="img">
                        <xsl:attribute name="class">cover</xsl:attribute>
                        <xsl:attribute name="src">
                            <xsl:value-of select="ImageUrl"/>
                        </xsl:attribute>
                    </xsl:element>
                    <div class="title">
                        <xsl:value-of select="Title"/>
                    </div>
                    <div class="authors">
                        <xsl:for-each select="Authors/Author">
                            <xsl:value-of select="Name"/><xsl:if test="position() != last()">,</xsl:if>
                        </xsl:for-each>
                    </div>
                    <div class="description">
                        <xsl:value-of select="Description"/>
                    </div>
                </div>
            </a>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
