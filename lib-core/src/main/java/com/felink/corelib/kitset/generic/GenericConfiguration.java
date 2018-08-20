package com.felink.corelib.kitset.generic;

import android.content.Context;

/**
 * @Description: </br>
 * @author: cxy </br>
 * @date: 2017年07月06日 14:59.</br>
 * @update: </br>
 */

public class GenericConfiguration {

    private IGenericUrlGenerator generator;
    private String pid;
    private int platform = 4;

    public GenericConfiguration(Builder builder) {
        this.generator = builder.urlGenerator;
        this.pid = builder.pid;
        this.platform = builder.mt;
    }

    public IGenericUrlGenerator getGenerator() {
        return generator;
    }

    public String getPid() {
        return pid;
    }

    public int getPlatform() {
        return platform;
    }

    public static final class Builder {

        IGenericUrlGenerator urlGenerator = new GenericUrlGenerator();
        String pid;
        int mt = 4;

        public Builder setPid(String pid) {
            this.pid = pid;
            return this;
        }

        public Builder setPlatform(int mt) {
            this.mt = mt;
            return this;
        }

        public Builder setGenerator(IGenericUrlGenerator generator) {
            this.urlGenerator = generator;
            return this;
        }

        public GenericConfiguration build() {
            return new GenericConfiguration(this);
        }

    }
}
