/*
 * Decompiled with CFR 0_118.
 */
package com.itu.util.can;

import com.itu.util.can.S_MSG;

public class UserType {

    public class PByte {
        public byte[] value;

        public PByte(int dimension) {
            this.value = new byte[dimension];
        }
    }

    public class PInt {
        public int[] value;

        public PInt(int dimension) {
            this.value = new int[dimension];
        }
    }

    public class PS_MSG {
        public S_MSG value;
        final /* synthetic */ UserType type;

        public PS_MSG(UserType userType, int dimension) {
            this.type = userType;
            this.value = new S_MSG(dimension);
        }

        public PS_MSG(UserType userType, byte[] buffer) {
            this.type = userType;
            this.value = new S_MSG(buffer);
        }
    }

    public class PShort {
        public short[] value;

        public PShort(int dimension) {
            this.value = new short[dimension];
        }
    }

}

