<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : AdminBooks.xsl
    Created on : March 28, 2013, 1:51 PM
    Author     : NoName
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html"/>

    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>
    <xsl:template match="*[local-name()='Books']">
        <table>
            <thead>
                <th>Book Title</th>
                <th>Category</th>
                <th>Publisher</th>
                <th>Price</th>
                <th></th>
                <th></th>
            </thead>
        <xsl:for-each select="*[local-name()='Book']">
            <xsl:element name="tr">
                <xsl:attribute name="class">data table-row</xsl:attribute>
                <xsl:attribute name="data-id">
                    <xsl:value-of select="*[local-name()='Id']"/>
                </xsl:attribute>
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
                
                <td class="info">
                    <xsl:value-of select="*[local-name()='Title']"/>
                </td>
                <td class="info">
                    <xsl:value-of select="*[local-name()='Category']/*[local-name()='Name']"/>
                </td>
                <td class="info">
                    <xsl:value-of select="*[local-name()='Publisher']/*[local-name()='Name']"/>
                </td>
                <td class="price">
                    <xsl:value-of select="*[local-name()='Price']"/>
                </td>
                <td class="info">
                    <a href="#editBook" class="btn edit" title="" data-modal="true">Edit</a>
                </td>
                <td class="info">
                    <a href="javascript:void(0)" class="btn delete" title="">Delete</a>
                </td>
            </xsl:element>
        </xsl:for-each>
        </table>
    </xsl:template>
</xsl:stylesheet>
