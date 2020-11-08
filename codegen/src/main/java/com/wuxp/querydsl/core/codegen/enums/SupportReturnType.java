package com.wuxp.querydsl.core.codegen.enums;

/**
 * 支持的返回值类型
 *
 * @author wuxp
 */
public enum SupportReturnType {

    /**
     * @see java.lang.String
     */
    String,

    /**
     * @see java.lang.Byte
     */
    Byte,

    /**
     * @see java.lang.Short
     */
    Short,

    /**
     * @see java.lang.Integer
     */
    Integer,

    /**
     * @see java.lang.Long
     */
    Long,

    /**
     * @see java.lang.Number
     */
    Number,

    /**
     * @see java.lang.Double
     */
    Double,

    /**
     * @see java.lang.Float
     */
    Float,

    /**
     * @see java.math.BigDecimal
     */
    BigDecimal,

    /**
     * @see java.math.BigInteger
     */
    BigInteger,

    /**
     * @see java.lang.Boolean
     */
    Boolean,

    /**
     * @see java.util.Date
     */
    Date,

    /**
     * @see java.time.LocalTime
     */
    LocalTime,

    /**
     * @see java.time.LocalDate
     */
    LocalDate,

    /**
     * @see java.time.LocalDateTime
     */
    LocalDateTime,


    /**
     * @see java.util.Map
     */
    Map,

    /**
     * @see java.util.List
     */
    List,

    /**
     * @see java.util.Set
     */
    Set,

    /**
     * @see java.util.Collection
     */
    Collection,

    /**
     * @see java.util.Optional
     */
    Optional;


    public static boolean isSimpleType(String simpleName) {
        SupportReturnType supportReturnType;
        try {
            supportReturnType = valueOf(simpleName);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return false;
        }
        boolean isCollectionType = SupportReturnType.Map.equals(supportReturnType) || SupportReturnType.List.equals(supportReturnType)
                || SupportReturnType.Set.equals(supportReturnType) || SupportReturnType.Collection.equals(supportReturnType)
                || SupportReturnType.Optional.equals(supportReturnType);
        return !isCollectionType;
    }
}
