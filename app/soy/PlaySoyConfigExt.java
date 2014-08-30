package soy;

import com.github.mati1979.play.soyplugin.ajax.SoyAjaxController;
import com.github.mati1979.play.soyplugin.ajax.allowedurls.DefaultSoyAllowedUrlsResolver;
import com.github.mati1979.play.soyplugin.ajax.allowedurls.SoyAllowedUrlsResolver;
import com.github.mati1979.play.soyplugin.ajax.auth.AuthManager;
import com.github.mati1979.play.soyplugin.ajax.auth.PermissableAuthManager;
import com.github.mati1979.play.soyplugin.ajax.hash.HashFileGenerator;
import com.github.mati1979.play.soyplugin.ajax.hash.MD5HashFileGenerator;
import com.github.mati1979.play.soyplugin.ajax.process.DefaultOutputProcessors;
import com.github.mati1979.play.soyplugin.ajax.process.OutputProcessors;
import com.github.mati1979.play.soyplugin.ajax.runtime.SoyHashesRuntimeDataResolver;
import com.github.mati1979.play.soyplugin.bundle.DefaultSoyMsgBundleResolver;
import com.github.mati1979.play.soyplugin.bundle.SoyMsgBundleResolver;
import com.github.mati1979.play.soyplugin.compile.DefaultTofuCompiler;
import com.github.mati1979.play.soyplugin.compile.TofuCompiler;
import com.github.mati1979.play.soyplugin.config.DefaultSoyViewConf;
import com.github.mati1979.play.soyplugin.config.SoyViewConf;
import com.github.mati1979.play.soyplugin.data.ReflectionToSoyDataConverter;
import com.github.mati1979.play.soyplugin.data.ToSoyDataConverter;
import com.github.mati1979.play.soyplugin.global.compile.CompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.compile.DefaultCompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.DefaultGlobalRuntimeModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.EmptyGlobalRuntimeModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.GlobalRuntimeModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.RuntimeDataResolver;
import com.github.mati1979.play.soyplugin.holder.CompiledTemplatesHolder;
import com.github.mati1979.play.soyplugin.holder.DefaultCompiledTemplatesHolder;
import com.github.mati1979.play.soyplugin.locale.LocaleProvider;
import com.github.mati1979.play.soyplugin.locale.PlayLocaleProvider;
import com.github.mati1979.play.soyplugin.plugin.DefaultSoy;
import com.github.mati1979.play.soyplugin.plugin.Soy;
import com.github.mati1979.play.soyplugin.render.DefaultTemplateRenderer;
import com.github.mati1979.play.soyplugin.render.TemplateRenderer;
import com.github.mati1979.play.soyplugin.template.FileSystemTemplateFilesResolver;
import com.github.mati1979.play.soyplugin.template.TemplateFilesResolver;
import com.google.common.collect.Lists;
import com.google.inject.AbstractModule;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.tofu.SoyTofuOptions;

import javax.inject.Qualifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mati on 02/02/2014.
 */
public class PlaySoyConfigExt extends AbstractModule {

//    @Bean
//    @Primary
//    public CompileTimeGlobalModelResolver soyCompileTimeGlobalModelResolver(final SoyViewConf soyViewConf)  {
//        final Map<String,Object> configData = new HashMap<>();
//        configData.put("soyplugin.global.hot.reload.mode", soyViewConf.globalHotReloadMode());
//        configData.put("soyplugin.precompile.templates", soyViewConf.compilePrecompileTemplates());
//
//        return new DefaultCompileTimeGlobalModelResolver(configData);
//    }
//
//    @Bean
//    @Primary
//    public GlobalRuntimeModelResolver soyRuntimeDataProvider(
//            final HashFileGenerator hashFileGenerator,
//            @Qualifier("assetsRuntimeDataResolver") final AssetsRuntimeResolver assetsRuntimeResolver) {
//        final List<RuntimeDataResolver> systemRuntimeResolvers = Lists.newArrayList();
//        systemRuntimeResolvers.add(new SoyHashesRuntimeDataResolver(hashFileGenerator));
//
//        final List<RuntimeDataResolver> userRuntimeResolvers = new ArrayList<>();
//        userRuntimeResolvers.add(new LoggedInRuntimeResolver());
//        userRuntimeResolvers.add(assetsRuntimeResolver);
//
//        return new DefaultGlobalRuntimeModelResolver(systemRuntimeResolvers, userRuntimeResolvers);
//    }
//
//    @Bean
//    @Qualifier("assetsRuntimeDataResolver")
//    public AssetsRuntimeResolver assetsRuntimeDataProvider() {
//        return new AssetsRuntimeResolver();
//    }
//
//    @Bean
//    @Primary
//    public SoyViewConf soyViewConf() {
//        return DefaultSoyViewConf.Builder.newBuilder()
//                .withAjaxAllowedUrls("template2.soy")
//                .withAjaxSecurityEnabled(true)
//                .build();
//    }
//
//    @Bean
//    @Primary
//    public LocaleProvider soyLocaleProvider() throws Exception {
//        return new PlayLocaleProvider();
//    }

    @Override
    protected void configure() {
        final DefaultSoyViewConf soyViewConf = soyViewConf();
        final DefaultCompileTimeGlobalModelResolver compileTimeGlobalModelResolver = compileTimeGlobalModelResolver(soyViewConf);

        bind(GlobalRuntimeModelResolver.class).to(EmptyGlobalRuntimeModelResolver.class);
        bind(TemplateFilesResolver.class).to(FileSystemTemplateFilesResolver.class);
        bind(CompileTimeGlobalModelResolver.class).toInstance(compileTimeGlobalModelResolver);
        bind(ToSoyDataConverter.class).to(ReflectionToSoyDataConverter.class);
        bind(SoyJsSrcOptions.class).toInstance(new SoyJsSrcOptions());
        bind(SoyTofuOptions.class).toInstance(new SoyTofuOptions());
        bind(TofuCompiler.class).to(DefaultTofuCompiler.class);
        bind(SoyMsgBundleResolver.class).to(DefaultSoyMsgBundleResolver.class);
        bind(CompiledTemplatesHolder.class).to(DefaultCompiledTemplatesHolder.class);
        bind(TemplateRenderer.class).to(DefaultTemplateRenderer.class);
        bind(SoyAllowedUrlsResolver.class).to(DefaultSoyAllowedUrlsResolver.class);
        bind(HashFileGenerator.class).to(MD5HashFileGenerator.class);
        bind(AuthManager.class).to(PermissableAuthManager.class);
        bind(OutputProcessors.class).to(DefaultOutputProcessors.class);
        bind(SoyAjaxController.class).asEagerSingleton();
        bind(SoyViewConf.class).toInstance(soyViewConf);
        bind(LocaleProvider.class).to(PlayLocaleProvider.class);
        bind(Soy.class).to(DefaultSoy.class);
    }

    private DefaultSoyViewConf soyViewConf() {
        return DefaultSoyViewConf.Builder.newBuilder()
                .withAjaxAllowedUrls("template2.soy")
                .withAjaxSecurityEnabled(true)
                .build();
    }

    private DefaultCompileTimeGlobalModelResolver compileTimeGlobalModelResolver(final SoyViewConf soyViewConf) {
        final Map<String,Object> configData = new HashMap();
        configData.put("soyplugin.global.hot.reload.mode", soyViewConf.globalHotReloadMode());
        configData.put("soyplugin.precompile.templates", soyViewConf.compilePrecompileTemplates());

        return new DefaultCompileTimeGlobalModelResolver(configData);
    }

//    private GlobalRuntimeModelResolver soyRuntimeDataProvider(
//            final HashFileGenerator hashFileGenerator,
//            @Qualifier("assetsRuntimeDataResolver") final AssetsRuntimeResolver assetsRuntimeResolver) {
//        final List<RuntimeDataResolver> systemRuntimeResolvers = Lists.newArrayList();
//        systemRuntimeResolvers.add(new SoyHashesRuntimeDataResolver(hashFileGenerator));
//
//        final List<RuntimeDataResolver> userRuntimeResolvers = new ArrayList<>();
//        userRuntimeResolvers.add(new LoggedInRuntimeResolver());
//        //userRuntimeResolvers.add(assetsRuntimeResolver);
//
//        return new DefaultGlobalRuntimeModelResolver(systemRuntimeResolvers, userRuntimeResolvers);
//    }

}
