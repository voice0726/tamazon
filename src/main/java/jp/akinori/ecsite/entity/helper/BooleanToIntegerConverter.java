package jp.akinori.ecsite.entity.helper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class BooleanToIntegerConverter implements AttributeConverter<Boolean, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Boolean aBoolean) {
        return aBoolean ? 1 : 0;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer aByte) {
        return aByte == 1;
    }
}
