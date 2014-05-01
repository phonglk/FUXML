<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : OrderDetails.xsl
    Created on : March 25, 2013, 11:07 PM
    Author     : NamNQ60475
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>
    <xsl:param name="pathFile" select="'web'"/>
    <xsl:template match="/*[local-name()='Order']">
        <fo:root xmlns:fo="http://www.w3.org/1999/XSL/Format">
            <fo:layout-master-set>
                <fo:simple-page-master master-name="x" page-height="8.5in"
            page-width="11in" margin-top="0.5in" margin-bottom="0.5in"
            margin-left="1in" margin-right="1in">
                    <fo:region-body margin-top="0.5in"/>
                    <fo:region-before extent="1in"/>
                    <fo:region-after extent="0.75in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="x">
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block text-align="left">
                        <fo:external-graphic width="50px" height="70px">
                            <xsl:attribute name="src">
                                <xsl:value-of select="concat($pathFile, *[local-name()='ImageURL'])"/>
                            </xsl:attribute>
                        </fo:external-graphic>
                    </fo:block>
                    <fo:block text-align="right" font-size="20pt" font-weight="bold"
                    color="blue" font-style="italic" text-decoration="underline">                        
                        XBooks Order Information
                    </fo:block>
                    <fo:block font-size="16pt" font-family="sans-serif"
                    line-height="24pt" color="blue" background-color="#C3C1C2"
                    space-after.optimum="15pt" text-align="center"
                    padding-top="3pt">
                        Order Details
                    </fo:block>
                </fo:static-content>
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="18pt" font-family="sans-serif"
                    line-height="24pt" space-after.optimum="15pt"
                    text-align="center" padding-top="3pt">
                    </fo:block>
                </fo:static-content>
                <fo:flow flow-name="xsl-region-body">
<!--                Value is not initalized, just use sample    -->
                    <fo:block text-align="left" space-after.optimum="15pt"
                        margin-top="100pt" color="red">
                        <fo:inline font-weight="bold" color="black">Order Code: </fo:inline>
                        <xsl:value-of select="*[local-name()='OrderCode']"/>
                    </fo:block>
                    <fo:block text-align="left" space-after.optimum="15pt">
                        <fo:inline font-weight="bold">Created Date: </fo:inline>
                        <xsl:value-of select="*[local-name()='Created' and text()]"/>
                    </fo:block>
                    <fo:block text-align="left" space-after.optimum="15pt">
                        <fo:inline font-weight="bold">Contact Name: </fo:inline>
                        <xsl:value-of select="*[local-name()='ContactName']"/>
                    </fo:block>
                    <fo:block text-align="left" space-after.optimum="15pt">
                        <fo:inline font-weight="bold">Phone: </fo:inline>
                        <xsl:value-of select="*[local-name()='Phone' and text()]"/>
                    </fo:block>
                    <fo:block text-align="left" space-after.optimum="15pt">
                        <fo:inline font-weight="bold">Address: </fo:inline>
                        <xsl:value-of select="*[local-name()='Address' and text()]"/>
                    </fo:block>
                   
                    <fo:block font-family="sans-serif">
                        <fo:table border-collapse="separate" table-layout="fixed"
                        space-after.optimum="15pt">
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="9cm"/>
                            <fo:table-column column-width="2cm"/>
                            <fo:table-column column-width="5cm"/>
                            <fo:table-column column-width="5cm"/>

                            <fo:table-body>
                                <fo:table-row background-color="#C3C1C2">
                                    <fo:table-cell border-color="blue"
                                    border-width="0.5pt"
                                    border-style="solid">
                                        <fo:block text-align="center" color="blue">No.</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                    border-width="0.5pt"
                                    border-style="solid">
                                        <fo:block text-align="center" color="blue">Book Title</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                    border-width="0.5pt"
                                    border-style="solid">
                                        <fo:block text-align="center" color="blue">Quantity</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                    border-width="0.5pt"
                                    border-style="solid">
                                        <fo:block text-align="center" color="blue">Price per unit</fo:block>
                                    </fo:table-cell>
                                    <fo:table-cell border-color="blue"
                                    border-width="0.5pt"
                                    border-style="solid">
                                        <fo:block text-align="center" color="blue">Total</fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
<!--                            Value is not initialized, just use sample    -->
                                <xsl:for-each select="*[local-name()='OrderDetails']/*[local-name()='OrderDetail']">
                                    <fo:table-row>
                                        <fo:table-cell border-color="blue"
                                            border-width="0.5pt"
                                            border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:number level="single" count="//*[local-name()='OrderDetail']"/>   <!-- count book -->
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                            border-width="0.5pt"
                                            border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:value-of select="*[local-name()='Title']"/>  <!-- Title of book -->
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                            border-width="0.5pt"
                                            border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:value-of select="*[local-name()='Quantity']"/>  <!-- Book's quantity -->
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                            border-width="0.5pt"
                                            border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:value-of select="*[local-name()='Price']"/>$  <!-- Price per book -->
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell border-color="blue"
                                            border-width="0.5pt"
                                            border-style="solid">
                                            <fo:block text-align="center">
                                                <xsl:value-of select="*[local-name()='TotalType']"/>$
<!--                                                *[local-name()='Price'] * *[local-name()='Quantity'] Total price (a kind of book) -->
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                    <fo:block text-align="right" font-size="12pt"
                    color="red" font-weight="bold">
<!--                         Create element OrderPrice to get sum of all Price book -->
                        <fo:inline>Order Total:</fo:inline>
                        <xsl:value-of select="*[local-name()='Total' and text()]"/>$
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>
