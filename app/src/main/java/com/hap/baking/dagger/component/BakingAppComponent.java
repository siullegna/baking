package com.hap.baking.dagger.component;

import com.hap.baking.dagger.module.ContextModule;
import com.hap.baking.dagger.module.DatabaseModule;
import com.hap.baking.dagger.module.NetworkModule;
import com.hap.baking.dagger.scope.ApplicationScope;

import dagger.Component;

/**
 * Created by luis on 12/4/17.
 */
@ApplicationScope
@Component(modules = {NetworkModule.class, DatabaseModule.class, ContextModule.class})
public interface BakingAppComponent extends AppGraph {
}
