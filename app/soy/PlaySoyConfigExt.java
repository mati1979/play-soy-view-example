package soy;

import com.github.mati1979.play.soyplugin.ajax.config.PlaySoyViewAjaxConfig;
import com.github.mati1979.play.soyplugin.ajax.hash.HashFileGenerator;
import com.github.mati1979.play.soyplugin.ajax.runtime.SoyHashesRuntimeDataResolver;
import com.github.mati1979.play.soyplugin.config.SoyViewConf;
import com.github.mati1979.play.soyplugin.global.compile.CompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.compile.DefaultCompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.DefaultGlobalRuntimeModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.GlobalRuntimeModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.RuntimeDataResolver;
import com.github.mati1979.play.soyplugin.locale.LocaleProvider;
import com.github.mati1979.play.soyplugin.locale.PlayLocaleProvider;
import com.google.common.collect.Lists;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mati on 02/02/2014.
 */
@Configuration
@Import(PlaySoyViewAjaxConfig.class)
public class PlaySoyConfigExt {

    @Bean
    @Primary
    public CompileTimeGlobalModelResolver soyCompileTimeGlobalModelResolver(final SoyViewConf soyViewConf) throws Exception {
        final Map<String,Object> configData = new HashMap<>();
        configData.put("soyplugin.global.hot.reload.mode", soyViewConf.globalHotReloadMode());
        configData.put("soyplugin.precompile.templates", soyViewConf.compilePrecompileTemplates());

        return new DefaultCompileTimeGlobalModelResolver(configData);
    }

    @Bean
    @Primary
    public GlobalRuntimeModelResolver soyRuntimeDataProvider(final HashFileGenerator hashFileGenerator) throws Exception {
        final List<RuntimeDataResolver> systemRuntimeResolvers = Lists.newArrayList();
        systemRuntimeResolvers.add(new SoyHashesRuntimeDataResolver(hashFileGenerator));

        final List<RuntimeDataResolver> userRuntimeResolvers = new ArrayList<>();
        userRuntimeResolvers.add(new LoggedInRuntimeResolver());

        return new DefaultGlobalRuntimeModelResolver(systemRuntimeResolvers, userRuntimeResolvers);
    }

    @Bean
    @Primary
    public LocaleProvider soyLocaleProvider() throws Exception {
        return new PlayLocaleProvider();
    }

}
