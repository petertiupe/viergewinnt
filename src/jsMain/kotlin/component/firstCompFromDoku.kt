package component

import dev.fritz2.dom.Window.clicks
import dev.fritz2.dom.html.Div
import dev.fritz2.dom.html.RenderContext

fun RenderContext.container(content: Div.() -> Unit): Div {

    return div("container") {
        content()
    }

}
/*
render {
    container {
        p {
            text("Hello World!")
        }

        clicks handledBy someHandler // you will see what this does in the next chapter
    }
}

*/