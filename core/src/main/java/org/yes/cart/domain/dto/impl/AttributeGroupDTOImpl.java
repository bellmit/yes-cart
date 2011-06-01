package org.yes.cart.domain.dto.impl;

import dp.lib.dto.geda.annotations.Dto;
import dp.lib.dto.geda.annotations.DtoField;
import org.yes.cart.domain.dto.AttributeGroupDTO;

/**
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
@Dto
public class AttributeGroupDTOImpl implements AttributeGroupDTO {

    private static final long serialVersionUID = 20100425L;

    @DtoField(value = "attributegroupId", readOnly = true)
    private long attributegroupId;

    @DtoField(value = "code", readOnly = true)
    private String code;

    @DtoField(value = "name", readOnly = true)
    private String name;

    @DtoField(value = "description", readOnly = true)
    private String description;


    /** {@inheritDoc} */
    public long getAttributegroupId() {
        return attributegroupId;
    }

    /**
     * {@inheritDoc}
     */
    public long getId() {
        return attributegroupId;
    }

    /** {@inheritDoc} */
    public void setAttributegroupId(final long attributegroupId) {
        this.attributegroupId = attributegroupId;
    }

    /** {@inheritDoc} */
    public String getCode() {
        return code;
    }

    /** {@inheritDoc} */
    public void setCode(final String code) {
        this.code = code;
    }

    /** {@inheritDoc} */
    public String getName() {
        return name;
    }

    /** {@inheritDoc} */
    public void setName(final String name) {
        this.name = name;
    }

    /** {@inheritDoc} */
    public String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    public void setDescription(final String description) {
        this.description = description;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AttributeGroupDTOImpl that = (AttributeGroupDTOImpl) o;

        if (!code.equals(that.code)) return false;
        if (!description.equals(that.description)) return false;
        if (!name.equals(that.name)) return false;

        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = code.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "AttributeGroupDTOImpl{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}