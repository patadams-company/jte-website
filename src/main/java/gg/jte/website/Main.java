package gg.jte.website;

import gg.jte.ContentType;
import gg.jte.TemplateEngine;
import gg.jte.output.StringOutput;
import gg.jte.resolve.DirectoryCodeResolver;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.staticfiles.Location;

import java.nio.file.Path;

public class Main {
    private static final boolean devSystem = System.getProperty("environment") == null;

    public static void main(String[] args) {
        TemplateEngine templateEngine = createTemplateEngine();

        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/assets", "assets", Location.EXTERNAL);
        }).start(7000);
        app.get("/", ctx -> render(ctx, templateEngine));
    }

    private static TemplateEngine createTemplateEngine() {
        DirectoryCodeResolver codeResolver = new DirectoryCodeResolver(Path.of("src", "main", "jte"));
        if (devSystem) {
            return TemplateEngine.create(codeResolver, ContentType.Html);
        } else {
            return TemplateEngine.createPrecompiled(Path.of("jte-classes"), ContentType.Html);
        }
    }

    private static void render(Context ctx, TemplateEngine templateEngine) {
        StringOutput output = new StringOutput();
        templateEngine.render("home.jte", new Page(), output);
        ctx.html(output.toString());
    }
}
