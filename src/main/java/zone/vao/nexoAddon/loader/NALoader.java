package zone.vao.nexoAddon.loader;

import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import java.util.List;

public class NALoader implements PluginLoader {
  private static final List<Repo> REPOS =
      List.of(
          new Repo("central", "https://repo1.maven.org/maven2/"),
          new Repo("jitpack", "https://jitpack.io/"),
          new Repo("nexo", "https://repo.nexomc.com/releases/")
      );

  private static final List<String> DEPENDS =
      List.of(
          "com.nexomc:protectionlib:1.0.8"
      );

  @Override
  public void classloader(PluginClasspathBuilder classpathBuilder) {

    MavenLibraryResolver resolver = new MavenLibraryResolver();

    for (Repo repo : REPOS) {
      resolver.addRepository(new RemoteRepository.Builder(repo.id(), "default", repo.url()).build());
    }

    for (String dep : DEPENDS) {
      resolver.addDependency(new Dependency(new DefaultArtifact(dep), null));
    }

    classpathBuilder.addLibrary(resolver);
  }
}