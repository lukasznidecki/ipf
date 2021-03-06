
package org.openehealth.ipf.commons.ihe.hpd.stub.dsmlv2;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ModifyDNRequest complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="ModifyDNRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{urn:oasis:names:tc:DSML:2:0:core}DsmlMessage"&gt;
 *       &lt;attribute name="dn" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" /&gt;
 *       &lt;attribute name="newrdn" use="required" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlRDN" /&gt;
 *       &lt;attribute name="deleteoldrdn" type="{http://www.w3.org/2001/XMLSchema}boolean" default="true" /&gt;
 *       &lt;attribute name="newSuperior" type="{urn:oasis:names:tc:DSML:2:0:core}DsmlDN" /&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ModifyDNRequest")
public class ModifyDNRequest
    extends DsmlMessage
{

    @XmlAttribute(name = "dn", required = true)
    protected String dn;
    @XmlAttribute(name = "newrdn", required = true)
    protected String newrdn;
    @XmlAttribute(name = "deleteoldrdn")
    protected Boolean deleteoldrdn;
    @XmlAttribute(name = "newSuperior")
    protected String newSuperior;

    /**
     * Gets the value of the dn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDn() {
        return dn;
    }

    /**
     * Sets the value of the dn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDn(String value) {
        this.dn = value;
    }

    /**
     * Gets the value of the newrdn property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewrdn() {
        return newrdn;
    }

    /**
     * Sets the value of the newrdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewrdn(String value) {
        this.newrdn = value;
    }

    /**
     * Gets the value of the deleteoldrdn property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isDeleteoldrdn() {
        if (deleteoldrdn == null) {
            return true;
        } else {
            return deleteoldrdn;
        }
    }

    /**
     * Sets the value of the deleteoldrdn property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setDeleteoldrdn(Boolean value) {
        this.deleteoldrdn = value;
    }

    /**
     * Gets the value of the newSuperior property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNewSuperior() {
        return newSuperior;
    }

    /**
     * Sets the value of the newSuperior property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNewSuperior(String value) {
        this.newSuperior = value;
    }

}
