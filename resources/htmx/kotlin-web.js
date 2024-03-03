(function () {
    function maybeProcessClasses(element: HTMLElement) {
        //add event handler if the class trigger is existing
        if (element.getAttribute("kw-class-trigger")) {
            let triggerEvent = element.getAttribute("kw-class-trigger")

            element.addEventListener(triggerEvent, evt => {
                processClassTrigger(element)
            })
        }
    }

    function processClassTrigger(element: HTMLElement) {
        const classList = element.classList

        //Toggle classes
        if (element.getAttribute("kw-class-toggle")) {
            let value = element.getAttribute("kw-class-toggle")
            let classes = value.split(" ")
            for (let clazz of classes) {
                if (classList.contains(clazz)) {
                    classList.remove(clazz)
                } else {
                    classList.add(clazz)
                }
            }
        }
        //Add classes
        if (element.getAttribute("kw-class-add")) {
            let value = element.getAttribute("kw-class-add")
            let classes = value.split(" ")
            classList.add(classes)
        }
        //Remove classes
        if (element.getAttribute("kw-class-remove")) {
            let value = element.getAttribute("kw-class-remove")
            let classes = value.split(" ")
            classList.remove(classes)
        }
    }

    htmx.defineExtension('kotlin-web', {
        onEvent: function (name, evt) {
            if (name === "htmx:afterSwap") {
                for (let styleElement of document.querySelectorAll('link[rel="stylesheet"].kw-style')) {
                    //style soll neuladen
                    let href = styleElement.getAttribute("href")
                    styleElement.setAttribute("href", href)
                }
            }

            if (name === "htmx:afterProcessNode") {
                let elt: HTMLElement = evt.detail.elt
                maybeProcessClasses(elt)
                if (elt.querySelectorAll) {
                    let children = elt.querySelectorAll("[kw-class-trigger]");
                    for (let i = 0; i < children.length; i++) {
                        maybeProcessClasses(children[i]);
                    }
                }
            }
        }
    });
})();