package sota.dto.response

import org.apache.commons.lang3.builder.{EqualsBuilder, HashCodeBuilder, ToStringBuilder, ToStringStyle}

trait AbstractResponse {

  val duration: Long

  override def toString: String = {
    ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE)
  }

  override def hashCode(): Int = {
    HashCodeBuilder.reflectionHashCode(this, false)
  }

  override def equals(obj: scala.Any): Boolean = {
    EqualsBuilder.reflectionEquals(this, obj, false)
  }

}
