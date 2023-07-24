/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2014 Red Hat, Inc., and individual contributors
 * as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wildfly.discovery;

import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
public final class GreaterEqualFilterSpec extends FilterSpec {

    private final String attribute;
    private final AttributeValue value;
    private transient int hashCode;

    GreaterEqualFilterSpec(final String attribute, final AttributeValue value) {
        this.attribute = attribute;
        this.value = value;
    }

    public boolean matchesSimple(final Map<String, AttributeValue> attributes) {
        final AttributeValue other = attributes.get(attribute);
        return value.compareTo(other) >= 0;
    }

    public boolean matchesMulti(final Map<String, ? extends Collection<AttributeValue>> attributes) {
        final Collection<AttributeValue> collection = attributes.get(attribute);
        if (collection != null) for (AttributeValue value : collection) {
            if (this.value.compareTo(value) >= 0) {
                return true;
            }
        }
        return false;
    }

    public boolean mayMatch(final Collection<String> attributeNames) {
        return attributeNames.contains(attribute);
    }

    public boolean mayNotMatch(final Collection<String> attributeNames) {
        return true;
    }

    public <P, R, E extends Exception> R accept(Visitor<P, R, E> visitor, P parameter) throws E {
        return visitor.handle(this, parameter);
    }

    /**
     * Get the attribute to compare.
     *
     * @return the attribute to compare (not {@code null})
     */
    public String getAttribute() {
        return attribute;
    }

    /**
     * Get the value to check for.
     *
     * @return the value to check for (not {@code null})
     */
    public AttributeValue getValue() {
        return value;
    }

    public int hashCode() {
        int hashCode = this.hashCode;
        if (hashCode == 0) {
            hashCode = (getClass().hashCode() * 19 + attribute.hashCode()) * 19 + value.hashCode();
            if (hashCode == 0) hashCode = 1 << 30;
            return this.hashCode = hashCode;
        }
        return hashCode;
    }

    public boolean equals(final FilterSpec other) {
        return other instanceof GreaterEqualFilterSpec && equals((GreaterEqualFilterSpec) other);
    }

    public boolean equals(final GreaterEqualFilterSpec other) {
        return this == other || other != null && attribute.equals(other.attribute) && value.equals(other.value);
    }

    void toString(final StringBuilder builder) {
        builder.append('(');
        FilterSpec.escapeTo(attribute, builder);
        builder.append('>');
        builder.append('=');
        builder.append(value);
        builder.append(')');
    }
}
