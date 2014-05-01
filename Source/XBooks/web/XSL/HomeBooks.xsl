<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="2.0">
    <xsl:output method="html"/>
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="*[local-name()='Books']">
        <xsl:for-each select="*[local-name()='Book']">
            <div class="item" draggable="true">
                <xsl:element name="div">
                    <xsl:attribute name="class">data</xsl:attribute>
                    <xsl:attribute name="data-id"><xsl:value-of select="*[local-name()='Id']"/></xsl:attribute>
                    <xsl:attribute name="data-json">
                        {
                            "Id":<xsl:value-of select="*[local-name()='Id']"/>,
                            "ISBN":"<xsl:value-of select="*[local-name()='ISBN']"/>",
                            "Title":"<xsl:value-of select="*[local-name()='Title']"/>",
                            "Category":"<xsl:value-of select="*[local-name()='Category']/*[local-name()='Name']"/>",
                            "Price":<xsl:value-of select="*[local-name()='Price']"/>,
                            "Description":"<xsl:value-of select="*[local-name()='Description']" />",
                            "ImageUrl":"<xsl:value-of select="*[local-name()='ImageUrl']"/>",
                            "Publisher":"<xsl:value-of select="*[local-name()='Publisher']/*[local-name()='Name']"/>",
                            "Authors":[<xsl:for-each select="*[local-name()='Authors']/*[local-name()='Author']">
                                "<xsl:value-of select="*[local-name()='Name']"/>"<xsl:if test="position() != last()">,</xsl:if>
                            </xsl:for-each>]
                        }
                    </xsl:attribute>
                </xsl:element>
                <xsl:element name="img">
                    <xsl:attribute name="class">cover</xsl:attribute>
                    <xsl:attribute name="src">
                        <xsl:value-of select="*[local-name()='ImageUrl']"/>
                    </xsl:attribute>
                </xsl:element>
                <div class="info">
                    <h2>
                        <xsl:value-of select="*[local-name()='Title']"/>
                    </h2>
                    <div class="description">
                        <xsl:value-of select="*[local-name()='Description']"/>
                    </div>
                    <div class="price">
                        <xsl:value-of select="*[local-name()='Price']"/>
                        $
                    </div>
                    <a href="javascript:void(0)" class="btn order" title="Add to cart">
                        <img src="./Resources/images/icon-cart.png"/>
                    </a>
                </div>
            </div>
        </xsl:for-each>
    </xsl:template>
</xsl:stylesheet>
