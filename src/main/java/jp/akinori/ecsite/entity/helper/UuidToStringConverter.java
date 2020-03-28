package jp.akinori.ecsite.entity.helper;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.UUID;

/**
 * {@link java.util.UUID} &lt;=&gt; {@link java.lang.String}変換を行うJPAコンバーター
 */
@Converter(autoApply = true)
public class UuidToStringConverter implements AttributeConverter<UUID, String> {

    @Override
    public String convertToDatabaseColumn(UUID attribute) {
        return attribute == null ? null : attribute.toString();
    }

    @Override
    public UUID convertToEntityAttribute(String dbData) {
        return dbData == null ? null : UUID.fromString(dbData);
    }
}
