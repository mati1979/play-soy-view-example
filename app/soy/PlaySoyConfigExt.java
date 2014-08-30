package soy;

import com.github.mati1979.play.soyplugin.ajax.SoyAjaxController;
import com.github.mati1979.play.soyplugin.ajax.allowedurls.DefaultSoyAllowedUrlsResolver;
import com.github.mati1979.play.soyplugin.ajax.allowedurls.SoyAllowedUrlsResolver;
import com.github.mati1979.play.soyplugin.ajax.auth.AuthManager;
import com.github.mati1979.play.soyplugin.ajax.auth.PermissableAuthManager;
import com.github.mati1979.play.soyplugin.ajax.hash.HashFileGenerator;
import com.github.mati1979.play.soyplugin.ajax.hash.MD5HashFileGenerator;
import com.github.mati1979.play.soyplugin.ajax.process.DefaultOutputProcessors;
import com.github.mati1979.play.soyplugin.ajax.process.OutputProcessor;
import com.github.mati1979.play.soyplugin.ajax.process.OutputProcessors;
import com.github.mati1979.play.soyplugin.ajax.process.google.GoogleClosureOutputProcessor;
import com.github.mati1979.play.soyplugin.ajax.runtime.SoyHashesRuntimeDataResolver;
import com.github.mati1979.play.soyplugin.bundle.DefaultSoyMsgBundleResolver;
import com.github.mati1979.play.soyplugin.bundle.SoyMsgBundleResolver;
import com.github.mati1979.play.soyplugin.compile.DefaultTofuCompiler;
import com.github.mati1979.play.soyplugin.compile.TofuCompiler;
import com.github.mati1979.play.soyplugin.config.ConfigDefaults;
import com.github.mati1979.play.soyplugin.config.DefaultConfigDefaults;
import com.github.mati1979.play.soyplugin.config.PlaySoyViewConf;
import com.github.mati1979.play.soyplugin.config.SoyViewConf;
import com.github.mati1979.play.soyplugin.data.ReflectionToSoyDataConverter;
import com.github.mati1979.play.soyplugin.data.ToSoyDataConverter;
import com.github.mati1979.play.soyplugin.global.compile.CompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.compile.DefaultCompileTimeGlobalModelResolver;
import com.github.mati1979.play.soyplugin.global.runtime.DefaultGlobalRuntimeModelResolver;
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
import com.google.inject.Provides;
import com.google.inject.name.Names;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.tofu.SoyTofuOptions;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mati on 02/02/2014.
 */
public class PlaySoyConfigExt extends AbstractModule {

    @Override
    protected void configure() {
        bind(TemplateFilesResolver.class).to(FileSystemTemplateFilesResolver.class).in(Singleton.class);
        bind(ToSoyDataConverter.class).to(ReflectionToSoyDataConverter.class).in(Singleton.class);
        bind(SoyJsSrcOptions.class).toInstance(new SoyJsSrcOptions());
        bind(SoyTofuOptions.class).toInstance(new SoyTofuOptions());
        bind(TofuCompiler.class).to(DefaultTofuCompiler.class).in(Singleton.class);
        bind(SoyMsgBundleResolver.class).to(DefaultSoyMsgBundleResolver.class).in(Singleton.class);
        bind(CompiledTemplatesHolder.class).to(DefaultCompiledTemplatesHolder.class).in(Singleton.class);
        bind(TemplateRenderer.class).to(DefaultTemplateRenderer.class).in(Singleton.class);
        bind(Soy.class).to(DefaultSoy.class).in(Singleton.class);
        bind(ConfigDefaults.class).to(DefaultConfigDefaults.class).in(Singleton.class);
        bind(SoyViewConf.class).to(PlaySoyViewConf.class).in(Singleton.class);


        bind(SoyAllowedUrlsResolver.class).to(DefaultSoyAllowedUrlsResolver.class).in(Singleton.class);
        bind(HashFileGenerator.class).to(MD5HashFileGenerator.class).in(Singleton.class);
        bind(AuthManager.class).to(PermissableAuthManager.class).in(Singleton.class);
        bind(OutputProcessor.class).annotatedWith(Names.named("google")).to(GoogleClosureOutputProcessor.class);
        bind(OutputProcessors.class).to(DefaultOutputProcessors.class).in(Singleton.class);
        bind(SoyAjaxController.class).in(Singleton.class);
        bind(LocaleProvider.class).to(PlayLocaleProvider.class).in(Singleton.class);
        bind(RuntimeDataResolver.class).annotatedWith(Names.named("soyHashes")).to(SoyHashesRuntimeDataResolver.class).in(Singleton.class);
        bind(RuntimeDataResolver.class).annotatedWith(Names.named("assets")).to(AssetsRuntimeResolver.class).in(Singleton.class);
        bind(RuntimeDataResolver.class).annotatedWith(Names.named("loggedIn")).to(LoggedInRuntimeResolver.class).in(Singleton.class);
    }

//    @Provides
//    @Singleton
//    SoyViewConf soyViewConff() {
//        return DefaultSoyViewConf.Builder.newBuilder()
//                .withAjaxAllowedUrls("template2.soy")
//                .withAjaxSecurityEnabled(true)
//                .build();
//    }

    @Provides
    @Singleton
    @Inject
    CompileTimeGlobalModelResolver compileTimeGlobalModelResolver(final SoyViewConf soyViewConf) {
        final Map<String, Object> configData = new HashMap<>();
        configData.put("soyplugin.global.hot.reload.mode", soyViewConf.globalHotReloadMode());
        configData.put("soyplugin.precompile.templates", soyViewConf.compilePrecompileTemplates());

        return new DefaultCompileTimeGlobalModelResolver(configData);
    }

    @Provides
    @Singleton
    @Inject
    GlobalRuntimeModelResolver soyRuntimeDataProvider(
            final @Named("soyHashes") RuntimeDataResolver soyHashes,
            final @Named("assets") RuntimeDataResolver assets,
            final @Named("loggedIn") RuntimeDataResolver loggedIn) {
        final List<RuntimeDataResolver> systemRuntimeResolvers = Lists.newArrayList();
        systemRuntimeResolvers.add(soyHashes);
        systemRuntimeResolvers.add(assets);
        systemRuntimeResolvers.add(loggedIn);

        return new DefaultGlobalRuntimeModelResolver(systemRuntimeResolvers, Lists.newArrayList());
    }

}
