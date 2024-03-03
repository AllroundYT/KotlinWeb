var htmx = function () {
    "use strict";
    const Q = {
        onLoad: z,
        process: Lt,
        on: xe,
        off: ye,
        trigger: fe,
        ajax: vn,
        find: r,
        findAll: m,
        closest: h,
        values: function (e, t) {
            const n = nn(e, t || "post");
            return n.values
        },
        remove: G,
        addClass: K,
        removeClass: o,
        toggleClass: Z,
        takeClass: W,
        swap: _e,
        defineExtension: In,
        removeExtension: Pn,
        logAll: $,
        logNone: J,
        logger: null,
        config: {
            historyEnabled: true,
            historyCacheSize: 10,
            refreshOnHistoryMiss: false,
            defaultSwapStyle: "innerHTML",
            defaultSwapDelay: 0,
            defaultSettleDelay: 20,
            includeIndicatorStyles: true,
            indicatorClass: "htmx-indicator",
            requestClass: "htmx-request",
            addedClass: "htmx-added",
            settlingClass: "htmx-settling",
            swappingClass: "htmx-swapping",
            allowEval: true,
            allowScriptTags: true,
            inlineScriptNonce: "",
            attributesToSettle: ["class", "style", "width", "height"],
            withCredentials: false,
            timeout: 0,
            wsReconnectDelay: "full-jitter",
            wsBinaryType: "blob",
            disableSelector: "[hx-disable], [data-hx-disable]",
            scrollBehavior: "instant",
            defaultFocusScroll: false,
            getCacheBusterParam: false,
            globalViewTransitions: false,
            methodsThatUseUrlParams: ["get", "delete"],
            selfRequestsOnly: true,
            ignoreTitle: false,
            scrollIntoViewOnBoost: true,
            triggerSpecsCache: null,
            disableInheritance: false,
            responseHandling: [{code: "204", swap: false}, {code: "[23]..", swap: true}, {
                code: "[45]..",
                swap: false,
                error: true
            }]
        },
        parseInterval: d,
        _: t,
        version: "2.0a"
    };
    const n = {
        addTriggerHandler: bt,
        bodyContains: le,
        canAccessLocalStorage: j,
        findThisElement: we,
        filterValues: ln,
        swap: _e,
        hasAttribute: s,
        getAttributeValue: te,
        getClosestAttributeValue: re,
        getClosestMatch: q,
        getExpressionVars: xn,
        getHeaders: sn,
        getInputValues: nn,
        getInternalData: ie,
        getSwapSpecification: cn,
        getTriggerSpecs: ot,
        getTarget: Se,
        makeFragment: D,
        mergeObjects: ue,
        makeSettleInfo: hn,
        oobSwap: Oe,
        querySelectorExt: ce,
        settleImmediately: jt,
        shouldCancel: ct,
        triggerEvent: fe,
        triggerErrorEvent: ae,
        withExtensions: kt
    };
    const E = ["get", "post", "put", "delete", "patch"];
    const R = E.map(function (e) {
        return "[hx-" + e + "], [data-hx-" + e + "]"
    }).join(", ");
    const O = e("head");

    function e(e, t = false) {
        return new RegExp(`<${e}(\\s[^>]*>|>)([\\s\\S]*?)<\\/${e}>`, t ? "gim" : "im")
    }

    function d(e) {
        if (e == undefined) {
            return undefined
        }
        let t = NaN;
        if (e.slice(-2) == "ms") {
            t = parseFloat(e.slice(0, -2))
        } else if (e.slice(-1) == "s") {
            t = parseFloat(e.slice(0, -1)) * 1e3
        } else if (e.slice(-1) == "m") {
            t = parseFloat(e.slice(0, -1)) * 1e3 * 60
        } else {
            t = parseFloat(e)
        }
        return isNaN(t) ? undefined : t
    }

    function ee(e, t) {
        return e.getAttribute && e.getAttribute(t)
    }

    function s(e, t) {
        return e.hasAttribute && (e.hasAttribute(t) || e.hasAttribute("data-" + t))
    }

    function te(e, t) {
        return ee(e, t) || ee(e, "data-" + t)
    }

    function u(e) {
        const t = e.parentElement;
        if (!t && e.parentNode instanceof ShadowRoot) return e.parentNode;
        return t
    }

    function ne() {
        return document
    }

    function H(e, t) {
        return e.getRootNode ? e.getRootNode({composed: t}) : ne()
    }

    function q(e, t) {
        while (e && !t(e)) {
            e = u(e)
        }
        return e || null
    }

    function T(e, t, n) {
        const r = te(t, n);
        const o = te(t, "hx-disinherit");
        var i = te(t, "hx-inherit");
        if (e !== t) {
            if (Q.config.disableInheritance) {
                if (i && (i === "*" || i.split(" ").indexOf(n) >= 0)) {
                    return r
                } else {
                    return null
                }
            }
            if (o && (o === "*" || o.split(" ").indexOf(n) >= 0)) {
                return "unset"
            }
        }
        return r
    }

    function re(t, n) {
        let r = null;
        q(t, function (e) {
            return r = T(t, e, n)
        });
        if (r !== "unset") {
            return r
        }
    }

    function f(e, t) {
        const n = e.matches || e.matchesSelector || e.msMatchesSelector || e.mozMatchesSelector || e.webkitMatchesSelector || e.oMatchesSelector;
        return n && n.call(e, t)
    }

    function N(e) {
        const t = /<([a-z][^\/\0>\x20\t\r\n\f]*)/i;
        const n = t.exec(e);
        if (n) {
            return n[1].toLowerCase()
        } else {
            return ""
        }
    }

    function L(e) {
        const t = new DOMParser;
        return t.parseFromString(e, "text/html")
    }

    function A(e, t) {
        while (t.childNodes.length > 0) {
            e.append(t.childNodes[0])
        }
    }

    function I(e) {
        const t = ne().createElement("script");
        se(e.attributes, function (e) {
            t.setAttribute(e.name, e.value)
        });
        t.textContent = e.textContent;
        t.async = false;
        if (Q.config.inlineScriptNonce) {
            t.nonce = Q.config.inlineScriptNonce
        }
        return t
    }

    function P(e) {
        return e.matches("script") && (e.type === "text/javascript" || e.type === "module" || e.type === "")
    }

    function k(e) {
        Array.from(e.querySelectorAll("script")).forEach(e => {
            if (P(e)) {
                const t = I(e);
                const n = e.parentNode;
                try {
                    n.insertBefore(t, e)
                } catch (e) {
                    v(e)
                } finally {
                    e.remove()
                }
            }
        })
    }

    function D(e) {
        const t = e.replace(O, "");
        const n = N(t);
        let r = null;
        if (n === "html") {
            r = new DocumentFragment;
            const i = L(e);
            A(r, i.body);
            r.title = i.title
        } else if (n === "body") {
            r = new DocumentFragment;
            const i = L(t);
            A(r, i.body);
            r.title = i.title
        } else {
            const i = L('<body><template class="internal-htmx-wrapper">' + t + "</template></body>");
            r = i.querySelector("template").content;
            r.title = i.title;
            var o = r.querySelector("title");
            if (o && o.parentNode === r) {
                o.remove();
                r.title = o.innerText
            }
        }
        if (r) {
            if (Q.config.allowScriptTags) {
                k(r)
            } else {
                r.querySelectorAll("script").forEach(e => e.remove())
            }
        }
        return r
    }

    function oe(e) {
        if (e) {
            e()
        }
    }

    function X(e, t) {
        return Object.prototype.toString.call(e) === "[object " + t + "]"
    }

    function M(e) {
        return X(e, "Function")
    }

    function F(e) {
        return X(e, "Object")
    }

    function ie(e) {
        const t = "htmx-internal-data";
        let n = e[t];
        if (!n) {
            n = e[t] = {}
        }
        return n
    }

    function U(t) {
        const n = [];
        if (t) {
            for (let e = 0; e < t.length; e++) {
                n.push(t[e])
            }
        }
        return n
    }

    function se(t, n) {
        if (t) {
            for (let e = 0; e < t.length; e++) {
                n(t[e])
            }
        }
    }

    function B(e) {
        const t = e.getBoundingClientRect();
        const n = t.top;
        const r = t.bottom;
        return n < window.innerHeight && r >= 0
    }

    function le(e) {
        if (e.getRootNode && e.getRootNode() instanceof window.ShadowRoot) {
            return ne().body.contains(e.getRootNode().host)
        } else {
            return ne().body.contains(e)
        }
    }

    function V(e) {
        return e.trim().split(/\s+/)
    }

    function ue(e, t) {
        for (const n in t) {
            if (t.hasOwnProperty(n)) {
                e[n] = t[n]
            }
        }
        return e
    }

    function w(e) {
        try {
            return JSON.parse(e)
        } catch (e) {
            v(e);
            return null
        }
    }

    function j() {
        const e = "htmx:localStorageTest";
        try {
            localStorage.setItem(e, e);
            localStorage.removeItem(e);
            return true
        } catch (e) {
            return false
        }
    }

    function _(t) {
        try {
            const e = new URL(t);
            if (e) {
                t = e.pathname + e.search
            }
            if (!/^\/$/.test(t)) {
                t = t.replace(/\/+$/, "")
            }
            return t
        } catch (e) {
            return t
        }
    }

    function t(e) {
        return gn(ne().body, function () {
            return eval(e)
        })
    }

    function z(t) {
        const e = Q.on("htmx:load", function (e) {
            t(e.detail.elt)
        });
        return e
    }

    function $() {
        Q.logger = function (e, t, n) {
            if (console) {
                console.log(t, e, n)
            }
        }
    }

    function J() {
        Q.logger = null
    }

    function r(e, t) {
        if (t) {
            return e.querySelector(t)
        } else {
            return r(ne(), e)
        }
    }

    function m(e, t) {
        if (t) {
            return e.querySelectorAll(t)
        } else {
            return m(ne(), e)
        }
    }

    function G(e, t) {
        e = x(e);
        if (t) {
            setTimeout(function () {
                G(e);
                e = null
            }, t)
        } else {
            u(e).removeChild(e)
        }
    }

    function K(e, t, n) {
        e = x(e);
        if (n) {
            setTimeout(function () {
                K(e, t);
                e = null
            }, n)
        } else {
            e.classList && e.classList.add(t)
        }
    }

    function o(e, t, n) {
        e = x(e);
        if (n) {
            setTimeout(function () {
                o(e, t);
                e = null
            }, n)
        } else {
            if (e.classList) {
                e.classList.remove(t);
                if (e.classList.length === 0) {
                    e.removeAttribute("class")
                }
            }
        }
    }

    function Z(e, t) {
        e = x(e);
        e.classList.toggle(t)
    }

    function W(e, t) {
        e = x(e);
        se(e.parentElement.children, function (e) {
            o(e, t)
        });
        K(e, t)
    }

    function h(e, t) {
        e = x(e);
        if (e.closest) {
            return e.closest(t)
        } else {
            do {
                if (e == null || f(e, t)) {
                    return e
                }
            } while (e = e && u(e));
            return null
        }
    }

    function l(e, t) {
        return e.substring(0, t.length) === t
    }

    function Y(e, t) {
        return e.substring(e.length - t.length) === t
    }

    function i(e) {
        const t = e.trim();
        if (l(t, "<") && Y(t, "/>")) {
            return t.substring(1, t.length - 2)
        } else {
            return t
        }
    }

    function a(e, t, n) {
        if (t.indexOf("closest ") === 0) {
            return [h(e, i(t.substr(8)))]
        } else if (t.indexOf("find ") === 0) {
            return [r(e, i(t.substr(5)))]
        } else if (t === "next") {
            return [e.nextElementSibling]
        } else if (t.indexOf("next ") === 0) {
            return [ge(e, i(t.substr(5)), !!n)]
        } else if (t === "previous") {
            return [e.previousElementSibling]
        } else if (t.indexOf("previous ") === 0) {
            return [pe(e, i(t.substr(9)), !!n)]
        } else if (t === "document") {
            return [document]
        } else if (t === "window") {
            return [window]
        } else if (t === "body") {
            return [document.body]
        } else if (t === "root") {
            return [H(e, !!n)]
        } else if (t.indexOf("global ") === 0) {
            return a(e, t.slice(7), true)
        } else {
            return H(e, !!n).querySelectorAll(i(t))
        }
    }

    var ge = function (t, e, n) {
        const r = H(t, n).querySelectorAll(e);
        for (let e = 0; e < r.length; e++) {
            const o = r[e];
            if (o.compareDocumentPosition(t) === Node.DOCUMENT_POSITION_PRECEDING) {
                return o
            }
        }
    };
    var pe = function (t, e, n) {
        const r = H(t, n).querySelectorAll(e);
        for (let e = r.length - 1; e >= 0; e--) {
            const o = r[e];
            if (o.compareDocumentPosition(t) === Node.DOCUMENT_POSITION_FOLLOWING) {
                return o
            }
        }
    };

    function ce(e, t) {
        if (t) {
            return a(e, t)[0]
        } else {
            return a(ne().body, e)[0]
        }
    }

    function x(e, t) {
        if (X(e, "String")) {
            return r(t || document, e)
        } else {
            return e
        }
    }

    function me(e, t, n) {
        if (M(t)) {
            return {target: ne().body, event: e, listener: t}
        } else {
            return {target: x(e), event: t, listener: n}
        }
    }

    function xe(t, n, r) {
        Xn(function () {
            const e = me(t, n, r);
            e.target.addEventListener(e.event, e.listener)
        });
        const e = M(n);
        return e ? n : r
    }

    function ye(t, n, r) {
        Xn(function () {
            const e = me(t, n, r);
            e.target.removeEventListener(e.event, e.listener)
        });
        return M(n) ? n : r
    }

    const be = ne().createElement("output");

    function ve(e, t) {
        const n = re(e, t);
        if (n) {
            if (n === "this") {
                return [we(e, t)]
            } else {
                const r = a(e, n);
                if (r.length === 0) {
                    v('The selector "' + n + '" on ' + t + " returned no matches!");
                    return [be]
                } else {
                    return r
                }
            }
        }
    }

    function we(e, t) {
        return q(e, function (e) {
            return te(e, t) != null
        })
    }

    function Se(e) {
        const t = re(e, "hx-target");
        if (t) {
            if (t === "this") {
                return we(e, "hx-target")
            } else {
                return ce(e, t)
            }
        } else {
            const n = ie(e);
            if (n.boosted) {
                return ne().body
            } else {
                return e
            }
        }
    }

    function Ce(t) {
        const n = Q.config.attributesToSettle;
        for (let e = 0; e < n.length; e++) {
            if (t === n[e]) {
                return true
            }
        }
        return false
    }

    function Ee(t, n) {
        se(t.attributes, function (e) {
            if (!n.hasAttribute(e.name) && Ce(e.name)) {
                t.removeAttribute(e.name)
            }
        });
        se(n.attributes, function (e) {
            if (Ce(e.name)) {
                t.setAttribute(e.name, e.value)
            }
        })
    }

    function Re(t, e) {
        const n = kn(e);
        for (let e = 0; e < n.length; e++) {
            const r = n[e];
            try {
                if (r.isInlineSwap(t)) {
                    return true
                }
            } catch (e) {
                v(e)
            }
        }
        return t === "outerHTML"
    }

    function Oe(e, o, i) {
        let t = "#" + ee(o, "id");
        let s = "outerHTML";
        if (e === "true") {
        } else if (e.indexOf(":") > 0) {
            s = e.substr(0, e.indexOf(":"));
            t = e.substr(e.indexOf(":") + 1, e.length)
        } else {
            s = e
        }
        const n = ne().querySelectorAll(t);
        if (n) {
            se(n, function (e) {
                let t;
                const n = o.cloneNode(true);
                t = ne().createDocumentFragment();
                t.appendChild(n);
                if (!Re(s, e)) {
                    t = n
                }
                const r = {shouldSwap: true, target: e, fragment: t};
                if (!fe(e, "htmx:oobBeforeSwap", r)) return;
                e = r.target;
                if (r.shouldSwap) {
                    Ve(s, e, e, t, i)
                }
                se(i.elts, function (e) {
                    fe(e, "htmx:oobAfterSwap", r)
                })
            });
            o.parentNode.removeChild(o)
        } else {
            o.parentNode.removeChild(o);
            ae(ne().body, "htmx:oobErrorNoTarget", {content: o})
        }
        return e
    }

    function He(e) {
        se(m(e, "[hx-preserve], [data-hx-preserve]"), function (e) {
            const t = te(e, "id");
            const n = ne().getElementById(t);
            if (n != null) {
                e.parentNode.replaceChild(n, e)
            }
        })
    }

    function qe(s, e, l) {
        se(e.querySelectorAll("[id]"), function (e) {
            const t = ee(e, "id");
            if (t && t.length > 0) {
                const n = t.replace("'", "\\'");
                const r = e.tagName.replace(":", "\\:");
                const o = s.querySelector(r + "[id='" + n + "']");
                if (o && o !== s) {
                    const i = e.cloneNode();
                    Ee(e, o);
                    l.tasks.push(function () {
                        Ee(e, i)
                    })
                }
            }
        })
    }

    function Te(e) {
        return function () {
            o(e, Q.config.addedClass);
            Lt(e);
            Ne(e);
            fe(e, "htmx:load")
        }
    }

    function Ne(e) {
        const t = "[autofocus]";
        const n = f(e, t) ? e : e.querySelector(t);
        if (n != null) {
            n.focus()
        }
    }

    function c(e, t, n, r) {
        qe(e, n, r);
        while (n.childNodes.length > 0) {
            const o = n.firstChild;
            K(o, Q.config.addedClass);
            e.insertBefore(o, t);
            if (o.nodeType !== Node.TEXT_NODE && o.nodeType !== Node.COMMENT_NODE) {
                r.tasks.push(Te(o))
            }
        }
    }

    function Le(e, t) {
        let n = 0;
        while (n < e.length) {
            t = (t << 5) - t + e.charCodeAt(n++) | 0
        }
        return t
    }

    function Ae(t) {
        let n = 0;
        if (t.attributes) {
            for (let e = 0; e < t.attributes.length; e++) {
                const r = t.attributes[e];
                if (r.value) {
                    n = Le(r.name, n);
                    n = Le(r.value, n)
                }
            }
        }
        return n
    }

    function Ie(t) {
        const n = ie(t);
        if (n.onHandlers) {
            for (let e = 0; e < n.onHandlers.length; e++) {
                const r = n.onHandlers[e];
                t.removeEventListener(r.event, r.listener)
            }
            delete n.onHandlers
        }
    }

    function Pe(e) {
        const t = ie(e);
        if (t.timeout) {
            clearTimeout(t.timeout)
        }
        if (t.listenerInfos) {
            se(t.listenerInfos, function (e) {
                if (e.on) {
                    e.on.removeEventListener(e.trigger, e.listener)
                }
            })
        }
        Ie(e);
        se(Object.keys(t), function (e) {
            delete t[e]
        })
    }

    function g(e) {
        fe(e, "htmx:beforeCleanupElement");
        Pe(e);
        if (e.children) {
            se(e.children, function (e) {
                g(e)
            })
        }
    }

    function ke(t, e, n) {
        let r;
        const o = t.previousSibling;
        c(u(t), t, e, n);
        if (o == null) {
            r = u(t).firstChild
        } else {
            r = o.nextSibling
        }
        n.elts = n.elts.filter(function (e) {
            return e !== t
        });
        while (r && r !== t) {
            if (r.nodeType === Node.ELEMENT_NODE) {
                n.elts.push(r)
            }
            r = r.nextElementSibling
        }
        g(t);
        t.remove()
    }

    function De(e, t, n) {
        return c(e, e.firstChild, t, n)
    }

    function Xe(e, t, n) {
        return c(u(e), e, t, n)
    }

    function Me(e, t, n) {
        return c(e, null, t, n)
    }

    function Fe(e, t, n) {
        return c(u(e), e.nextSibling, t, n)
    }

    function Ue(e, t, n) {
        g(e);
        return u(e).removeChild(e)
    }

    function Be(e, t, n) {
        const r = e.firstChild;
        c(e, r, t, n);
        if (r) {
            while (r.nextSibling) {
                g(r.nextSibling);
                e.removeChild(r.nextSibling)
            }
            g(r);
            e.removeChild(r)
        }
    }

    function Ve(t, e, n, r, o) {
        switch (t) {
            case"none":
                return;
            case"outerHTML":
                ke(n, r, o);
                return;
            case"afterbegin":
                De(n, r, o);
                return;
            case"beforebegin":
                Xe(n, r, o);
                return;
            case"beforeend":
                Me(n, r, o);
                return;
            case"afterend":
                Fe(n, r, o);
                return;
            case"delete":
                Ue(n, r, o);
                return;
            default:
                var i = kn(e);
                for (let e = 0; e < i.length; e++) {
                    const s = i[e];
                    try {
                        const l = s.handleSwap(t, n, r, o);
                        if (l) {
                            if (typeof l.length !== "undefined") {
                                for (let e = 0; e < l.length; e++) {
                                    const u = l[e];
                                    if (u.nodeType !== Node.TEXT_NODE && u.nodeType !== Node.COMMENT_NODE) {
                                        o.tasks.push(Te(u))
                                    }
                                }
                            }
                            return
                        }
                    } catch (e) {
                        v(e)
                    }
                }
                if (t === "innerHTML") {
                    Be(n, r, o)
                } else {
                    Ve(Q.config.defaultSwapStyle, e, n, r, o)
                }
        }
    }

    function je(e, n) {
        se(m(e, "[hx-swap-oob], [data-hx-swap-oob]"), function (e) {
            const t = te(e, "hx-swap-oob");
            if (t != null) {
                Oe(t, e, n)
            }
        })
    }

    function _e(e, t, n, r) {
        if (!r) {
            r = {}
        }
        e = x(e);
        const o = document.activeElement;
        let i = {};
        try {
            i = {elt: o, start: o ? o.selectionStart : null, end: o ? o.selectionEnd : null}
        } catch (e) {
        }
        const s = hn(e);
        let l = D(t);
        s.title = l.title;
        if (r.selectOOB) {
            const c = r.selectOOB.split(",");
            for (let t = 0; t < c.length; t++) {
                const a = c[t].split(":", 2);
                let e = a[0].trim();
                if (e.indexOf("#") === 0) {
                    e = e.substring(1)
                }
                const f = a[1] || "true";
                const h = l.querySelector("#" + e);
                if (h) {
                    Oe(f, h, s)
                }
            }
        }
        je(l, s);
        se(m(l, "template"), function (e) {
            je(e.content, s);
            if (e.content.childElementCount === 0) {
                e.remove()
            }
        });
        if (r.select) {
            const d = ne().createDocumentFragment();
            se(l.querySelectorAll(r.select), function (e) {
                d.appendChild(e)
            });
            l = d
        }
        He(l);
        Ve(n.swapStyle, r.contextElement, e, l, s);
        if (i.elt && !le(i.elt) && ee(i.elt, "id")) {
            const g = document.getElementById(ee(i.elt, "id"));
            const p = {preventScroll: n.focusScroll !== undefined ? !n.focusScroll : !Q.config.defaultFocusScroll};
            if (g) {
                if (i.start && g.setSelectionRange) {
                    try {
                        g.setSelectionRange(i.start, i.end)
                    } catch (e) {
                    }
                }
                g.focus(p)
            }
        }
        e.classList.remove(Q.config.swappingClass);
        se(s.elts, function (e) {
            if (e.classList) {
                e.classList.add(Q.config.settlingClass)
            }
            fe(e, "htmx:afterSwap", r.eventInfo)
        });
        if (r.afterSwapCallback) {
            r.afterSwapCallback()
        }
        if (!n.ignoreTitle) {
            Tn(s.title)
        }
        const u = function () {
            se(s.tasks, function (e) {
                e.call()
            });
            se(s.elts, function (e) {
                if (e.classList) {
                    e.classList.remove(Q.config.settlingClass)
                }
                fe(e, "htmx:afterSettle", r.eventInfo)
            });
            if (r.anchor) {
                const e = x("#" + r.anchor);
                if (e) {
                    e.scrollIntoView({block: "start", behavior: "auto"})
                }
            }
            dn(s.elts, n);
            if (r.afterSettleCallback) {
                r.afterSettleCallback()
            }
        };
        if (n.settleDelay > 0) {
            setTimeout(u, n.settleDelay)
        } else {
            u()
        }
    }

    function ze(e, t, n) {
        const r = e.getResponseHeader(t);
        if (r.indexOf("{") === 0) {
            const o = w(r);
            for (const i in o) {
                if (o.hasOwnProperty(i)) {
                    let e = o[i];
                    if (!F(e)) {
                        e = {value: e}
                    }
                    fe(n, i, e)
                }
            }
        } else {
            const s = r.split(",");
            for (let e = 0; e < s.length; e++) {
                fe(n, s[e].trim(), [])
            }
        }
    }

    const $e = /\s/;
    const p = /[\s,]/;
    const Je = /[_$a-zA-Z]/;
    const Ge = /[_$a-zA-Z0-9]/;
    const Ke = ['"', "'", "/"];
    const y = /[^\s]/;
    const Ze = /[{(]/;
    const We = /[})]/;

    function Ye(e) {
        const t = [];
        let n = 0;
        while (n < e.length) {
            if (Je.exec(e.charAt(n))) {
                var r = n;
                while (Ge.exec(e.charAt(n + 1))) {
                    n++
                }
                t.push(e.substr(r, n - r + 1))
            } else if (Ke.indexOf(e.charAt(n)) !== -1) {
                const o = e.charAt(n);
                var r = n;
                n++;
                while (n < e.length && e.charAt(n) !== o) {
                    if (e.charAt(n) === "\\") {
                        n++
                    }
                    n++
                }
                t.push(e.substr(r, n - r + 1))
            } else {
                const i = e.charAt(n);
                t.push(i)
            }
            n++
        }
        return t
    }

    function Qe(e, t, n) {
        return Je.exec(e.charAt(0)) && e !== "true" && e !== "false" && e !== "this" && e !== n && t !== "."
    }

    function et(r, o, i) {
        if (o[0] === "[") {
            o.shift();
            let e = 1;
            let t = " return (function(" + i + "){ return (";
            let n = null;
            while (o.length > 0) {
                const s = o[0];
                if (s === "]") {
                    e--;
                    if (e === 0) {
                        if (n === null) {
                            t = t + "true"
                        }
                        o.shift();
                        t += ")})";
                        try {
                            const l = gn(r, function () {
                                return Function(t)()
                            }, function () {
                                return true
                            });
                            l.source = t;
                            return l
                        } catch (e) {
                            ae(ne().body, "htmx:syntax:error", {error: e, source: t});
                            return null
                        }
                    }
                } else if (s === "[") {
                    e++
                }
                if (Qe(s, n, i)) {
                    t += "((" + i + "." + s + ") ? (" + i + "." + s + ") : (window." + s + "))"
                } else {
                    t = t + s
                }
                n = o.shift()
            }
        }
    }

    function b(e, t) {
        let n = "";
        while (e.length > 0 && !t.test(e[0])) {
            n += e.shift()
        }
        return n
    }

    function tt(e) {
        let t;
        if (e.length > 0 && Ze.test(e[0])) {
            e.shift();
            t = b(e, We).trim();
            e.shift()
        } else {
            t = b(e, p)
        }
        return t
    }

    const nt = "input, textarea, select";

    function rt(e, t, n) {
        const r = [];
        const o = Ye(t);
        do {
            b(o, y);
            const l = o.length;
            const u = b(o, /[,\[\s]/);
            if (u !== "") {
                if (u === "every") {
                    const c = {trigger: "every"};
                    b(o, y);
                    c.pollInterval = d(b(o, /[,\[\s]/));
                    b(o, y);
                    var i = et(e, o, "event");
                    if (i) {
                        c.eventFilter = i
                    }
                    r.push(c)
                } else {
                    const a = {trigger: u};
                    var i = et(e, o, "event");
                    if (i) {
                        a.eventFilter = i
                    }
                    while (o.length > 0 && o[0] !== ",") {
                        b(o, y);
                        const f = o.shift();
                        if (f === "changed") {
                            a.changed = true
                        } else if (f === "once") {
                            a.once = true
                        } else if (f === "consume") {
                            a.consume = true
                        } else if (f === "delay" && o[0] === ":") {
                            o.shift();
                            a.delay = d(b(o, p))
                        } else if (f === "from" && o[0] === ":") {
                            o.shift();
                            if (Ze.test(o[0])) {
                                var s = tt(o)
                            } else {
                                var s = b(o, p);
                                if (s === "closest" || s === "find" || s === "next" || s === "previous") {
                                    o.shift();
                                    const h = tt(o);
                                    if (h.length > 0) {
                                        s += " " + h
                                    }
                                }
                            }
                            a.from = s
                        } else if (f === "target" && o[0] === ":") {
                            o.shift();
                            a.target = tt(o)
                        } else if (f === "throttle" && o[0] === ":") {
                            o.shift();
                            a.throttle = d(b(o, p))
                        } else if (f === "queue" && o[0] === ":") {
                            o.shift();
                            a.queue = b(o, p)
                        } else if (f === "root" && o[0] === ":") {
                            o.shift();
                            a[f] = tt(o)
                        } else if (f === "threshold" && o[0] === ":") {
                            o.shift();
                            a[f] = b(o, p)
                        } else {
                            ae(e, "htmx:syntax:error", {token: o.shift()})
                        }
                    }
                    r.push(a)
                }
            }
            if (o.length === l) {
                ae(e, "htmx:syntax:error", {token: o.shift()})
            }
            b(o, y)
        } while (o[0] === "," && o.shift());
        if (n) {
            n[t] = r
        }
        return r
    }

    function ot(e) {
        const t = te(e, "hx-trigger");
        let n = [];
        if (t) {
            const r = Q.config.triggerSpecsCache;
            n = r && r[t] || rt(e, t, r)
        }
        if (n.length > 0) {
            return n
        } else if (f(e, "form")) {
            return [{trigger: "submit"}]
        } else if (f(e, 'input[type="button"], input[type="submit"]')) {
            return [{trigger: "click"}]
        } else if (f(e, nt)) {
            return [{trigger: "change"}]
        } else {
            return [{trigger: "click"}]
        }
    }

    function it(e) {
        ie(e).cancelled = true
    }

    function st(e, t, n) {
        const r = ie(e);
        r.timeout = setTimeout(function () {
            if (le(e) && r.cancelled !== true) {
                if (!ft(n, e, It("hx:poll:trigger", {triggerSpec: n, target: e}))) {
                    t(e)
                }
                st(e, t, n)
            }
        }, n.pollInterval)
    }

    function lt(e) {
        return location.hostname === e.hostname && ee(e, "href") && ee(e, "href").indexOf("#") !== 0
    }

    function ut(t, o, e) {
        if (t.tagName === "A" && lt(t) && (t.target === "" || t.target === "_self") || t.tagName === "FORM") {
            o.boosted = true;
            let n, r;
            if (t.tagName === "A") {
                n = "get";
                r = ee(t, "href")
            } else {
                const i = ee(t, "method");
                n = i ? i.toLowerCase() : "get";
                if (n === "get") {
                }
                r = ee(t, "action")
            }
            e.forEach(function (e) {
                ht(t, function (e, t) {
                    if (h(e, Q.config.disableSelector)) {
                        g(e);
                        return
                    }
                    de(n, r, e, t)
                }, o, e, true)
            })
        }
    }

    function ct(e, t) {
        if (e.type === "submit" || e.type === "click") {
            if (t.tagName === "FORM") {
                return true
            }
            if (f(t, 'input[type="submit"], button') && h(t, "form") !== null) {
                return true
            }
            if (t.tagName === "A" && t.href && (t.getAttribute("href") === "#" || t.getAttribute("href").indexOf("#") !== 0)) {
                return true
            }
        }
        return false
    }

    function at(e, t) {
        return ie(e).boosted && e.tagName === "A" && t.type === "click" && (t.ctrlKey || t.metaKey)
    }

    function ft(e, t, n) {
        const r = e.eventFilter;
        if (r) {
            try {
                return r.call(t, n) !== true
            } catch (e) {
                ae(ne().body, "htmx:eventFilter:error", {error: e, source: r.source});
                return true
            }
        }
        return false
    }

    function ht(i, s, e, l, u) {
        const c = ie(i);
        let t;
        if (l.from) {
            t = a(i, l.from)
        } else {
            t = [i]
        }
        if (l.changed) {
            t.forEach(function (e) {
                const t = ie(e);
                t.lastValue = e.value
            })
        }
        se(t, function (r) {
            const o = function (e) {
                if (!le(i)) {
                    r.removeEventListener(l.trigger, o);
                    return
                }
                if (at(i, e)) {
                    return
                }
                if (u || ct(e, i)) {
                    e.preventDefault()
                }
                if (ft(l, i, e)) {
                    return
                }
                const t = ie(e);
                t.triggerSpec = l;
                if (t.handledFor == null) {
                    t.handledFor = []
                }
                if (t.handledFor.indexOf(i) < 0) {
                    t.handledFor.push(i);
                    if (l.consume) {
                        e.stopPropagation()
                    }
                    if (l.target && e.target) {
                        if (!f(e.target, l.target)) {
                            return
                        }
                    }
                    if (l.once) {
                        if (c.triggeredOnce) {
                            return
                        } else {
                            c.triggeredOnce = true
                        }
                    }
                    if (l.changed) {
                        const n = ie(r);
                        if (n.lastValue === r.value) {
                            return
                        }
                        n.lastValue = r.value
                    }
                    if (c.delayed) {
                        clearTimeout(c.delayed)
                    }
                    if (c.throttle) {
                        return
                    }
                    if (l.throttle > 0) {
                        if (!c.throttle) {
                            s(i, e);
                            c.throttle = setTimeout(function () {
                                c.throttle = null
                            }, l.throttle)
                        }
                    } else if (l.delay > 0) {
                        c.delayed = setTimeout(function () {
                            s(i, e)
                        }, l.delay)
                    } else {
                        fe(i, "htmx:trigger");
                        s(i, e)
                    }
                }
            };
            if (e.listenerInfos == null) {
                e.listenerInfos = []
            }
            e.listenerInfos.push({trigger: l.trigger, listener: o, on: r});
            r.addEventListener(l.trigger, o)
        })
    }

    let dt = false;
    let gt = null;

    function pt() {
        if (!gt) {
            gt = function () {
                dt = true
            };
            window.addEventListener("scroll", gt);
            setInterval(function () {
                if (dt) {
                    dt = false;
                    se(ne().querySelectorAll("[hx-trigger*='revealed'],[data-hx-trigger*='revealed']"), function (e) {
                        mt(e)
                    })
                }
            }, 200)
        }
    }

    function mt(t) {
        if (!s(t, "data-hx-revealed") && B(t)) {
            t.setAttribute("data-hx-revealed", "true");
            const e = ie(t);
            if (e.initHash) {
                fe(t, "revealed")
            } else {
                t.addEventListener("htmx:afterProcessNode", function (e) {
                    fe(t, "revealed")
                }, {once: true})
            }
        }
    }

    function xt(e, t, n, r) {
        const o = function () {
            if (!n.loaded) {
                n.loaded = true;
                t(e)
            }
        };
        if (r > 0) {
            setTimeout(o, r)
        } else {
            o()
        }
    }

    function yt(t, o, e) {
        let i = false;
        se(E, function (n) {
            if (s(t, "hx-" + n)) {
                const r = te(t, "hx-" + n);
                i = true;
                o.path = r;
                o.verb = n;
                e.forEach(function (e) {
                    bt(t, e, o, function (e, t) {
                        if (h(e, Q.config.disableSelector)) {
                            g(e);
                            return
                        }
                        de(n, r, e, t)
                    })
                })
            }
        });
        return i
    }

    function bt(r, e, t, n) {
        if (e.trigger === "revealed") {
            pt();
            ht(r, n, t, e);
            mt(r)
        } else if (e.trigger === "intersect") {
            const o = {};
            if (e.root) {
                o.root = ce(r, e.root)
            }
            if (e.threshold) {
                o.threshold = parseFloat(e.threshold)
            }
            const i = new IntersectionObserver(function (t) {
                for (let e = 0; e < t.length; e++) {
                    const n = t[e];
                    if (n.isIntersecting) {
                        fe(r, "intersect");
                        break
                    }
                }
            }, o);
            i.observe(r);
            ht(r, n, t, e)
        } else if (e.trigger === "load") {
            if (!ft(e, r, It("load", {elt: r}))) {
                xt(r, n, t, e.delay)
            }
        } else if (e.pollInterval > 0) {
            t.polling = true;
            st(r, n, e)
        } else {
            ht(r, n, t, e)
        }
    }

    function vt(e) {
        const t = e.attributes;
        for (let e = 0; e < t.length; e++) {
            const n = t[e].name;
            if (l(n, "hx-on:") || l(n, "data-hx-on:") || l(n, "hx-on-") || l(n, "data-hx-on-")) {
                return true
            }
        }
        return false
    }

    function wt(e) {
        let t = null;
        const n = [];
        if (!(e instanceof ShadowRoot)) {
            if (vt(e)) {
                n.push(e)
            }
            const r = document.evaluate('.//*[@*[ starts-with(name(), "hx-on:") or starts-with(name(), "data-hx-on:") or' + ' starts-with(name(), "hx-on-") or starts-with(name(), "data-hx-on-") ]]', e);
            while (t = r.iterateNext()) n.push(t)
        }
        return n
    }

    function St(e) {
        if (e.querySelectorAll) {
            const t = ", [hx-boost] a, [data-hx-boost] a, a[hx-boost], a[data-hx-boost]";
            const n = e.querySelectorAll(R + t + ", form, [type='submit']," + " [hx-ext], [data-hx-ext], [hx-trigger], [data-hx-trigger]");
            return n
        } else {
            return []
        }
    }

    function Ct(e) {
        const t = h(e.target, "button, input[type='submit']");
        const n = Rt(e);
        if (n) {
            n.lastButtonClicked = t
        }
    }

    function Et(e) {
        const t = Rt(e);
        if (t) {
            t.lastButtonClicked = null
        }
    }

    function Rt(e) {
        const t = h(e.target, "button, input[type='submit']");
        if (!t) {
            return
        }
        const n = x("#" + ee(t, "form"), t.getRootNode()) || h(t, "form");
        if (!n) {
            return
        }
        return ie(n)
    }

    function Ot(e) {
        e.addEventListener("click", Ct);
        e.addEventListener("focusin", Ct);
        e.addEventListener("focusout", Et)
    }

    function Ht(e) {
        const t = Ye(e);
        let n = 0;
        for (let e = 0; e < t.length; e++) {
            const r = t[e];
            if (r === "{") {
                n++
            } else if (r === "}") {
                n--
            }
        }
        return n
    }

    function qt(t, e, n) {
        const r = ie(t);
        if (!Array.isArray(r.onHandlers)) {
            r.onHandlers = []
        }
        let o;
        const i = function (e) {
            return gn(t, function () {
                if (!o) {
                    o = new Function("event", n)
                }
                o.call(t, e)
            })
        };
        t.addEventListener(e, i);
        r.onHandlers.push({event: e, listener: i})
    }

    function Tt(t) {
        Ie(t);
        for (let e = 0; e < t.attributes.length; e++) {
            const n = t.attributes[e].name;
            const r = t.attributes[e].value;
            if (l(n, "hx-on") || l(n, "data-hx-on")) {
                const o = n.indexOf("-on") + 3;
                const i = n.slice(o, o + 1);
                if (i === "-" || i === ":") {
                    let e = n.slice(o + 1);
                    if (l(e, ":")) {
                        e = "htmx" + e
                    } else if (l(e, "-")) {
                        e = "htmx:" + e.slice(1)
                    } else if (l(e, "htmx-")) {
                        e = "htmx:" + e.slice(5)
                    }
                    qt(t, e, r)
                }
            }
        }
    }

    function Nt(t) {
        if (h(t, Q.config.disableSelector)) {
            g(t);
            return
        }
        const n = ie(t);
        if (n.initHash !== Ae(t)) {
            Pe(t);
            n.initHash = Ae(t);
            fe(t, "htmx:beforeProcessNode");
            if (t.value) {
                n.lastValue = t.value
            }
            const e = ot(t);
            const r = yt(t, n, e);
            if (!r) {
                if (re(t, "hx-boost") === "true") {
                    ut(t, n, e)
                } else if (s(t, "hx-trigger")) {
                    e.forEach(function (e) {
                        bt(t, e, n, function () {
                        })
                    })
                }
            }
            if (t.tagName === "FORM" || ee(t, "type") === "submit" && s(t, "form")) {
                Ot(t)
            }
            fe(t, "htmx:afterProcessNode")
        }
    }

    function Lt(e) {
        e = x(e);
        if (h(e, Q.config.disableSelector)) {
            g(e);
            return
        }
        Nt(e);
        se(St(e), function (e) {
            Nt(e)
        });
        se(wt(e), Tt)
    }

    function At(e) {
        return e.replace(/([a-z0-9])([A-Z])/g, "$1-$2").toLowerCase()
    }

    function It(e, t) {
        let n;
        if (window.CustomEvent && typeof window.CustomEvent === "function") {
            n = new CustomEvent(e, {bubbles: true, cancelable: true, composed: true, detail: t})
        } else {
            n = ne().createEvent("CustomEvent");
            n.initCustomEvent(e, true, true, t)
        }
        return n
    }

    function ae(e, t, n) {
        fe(e, t, ue({error: t}, n))
    }

    function Pt(e) {
        return e === "htmx:afterProcessNode"
    }

    function kt(e, t) {
        se(kn(e), function (e) {
            try {
                t(e)
            } catch (e) {
                v(e)
            }
        })
    }

    function v(e) {
        if (console.error) {
            console.error(e)
        } else if (console.log) {
            console.log("ERROR: ", e)
        }
    }

    function fe(e, t, n) {
        e = x(e);
        if (n == null) {
            n = {}
        }
        n.elt = e;
        const r = It(t, n);
        if (Q.logger && !Pt(t)) {
            Q.logger(e, t, n)
        }
        if (n.error) {
            v(n.error);
            fe(e, "htmx:error", {errorInfo: n})
        }
        let o = e.dispatchEvent(r);
        const i = At(t);
        if (o && i !== t) {
            const s = It(i, r.detail);
            o = o && e.dispatchEvent(s)
        }
        kt(e, function (e) {
            o = o && (e.onEvent(t, r) !== false && !r.defaultPrevented)
        });
        return o
    }

    let S = location.pathname + location.search;

    function Dt() {
        const e = ne().querySelector("[hx-history-elt],[data-hx-history-elt]");
        return e || ne().body
    }

    function Xt(t, e) {
        if (!j()) {
            return
        }
        const n = Ft(e);
        const r = ne().title;
        const o = window.scrollY;
        if (Q.config.historyCacheSize <= 0) {
            localStorage.removeItem("htmx-history-cache");
            return
        }
        t = _(t);
        const i = w(localStorage.getItem("htmx-history-cache")) || [];
        for (let e = 0; e < i.length; e++) {
            if (i[e].url === t) {
                i.splice(e, 1);
                break
            }
        }
        const s = {url: t, content: n, title: r, scroll: o};
        fe(ne().body, "htmx:historyItemCreated", {item: s, cache: i});
        i.push(s);
        while (i.length > Q.config.historyCacheSize) {
            i.shift()
        }
        while (i.length > 0) {
            try {
                localStorage.setItem("htmx-history-cache", JSON.stringify(i));
                break
            } catch (e) {
                ae(ne().body, "htmx:historyCacheError", {cause: e, cache: i});
                i.shift()
            }
        }
    }

    function Mt(t) {
        if (!j()) {
            return null
        }
        t = _(t);
        const n = w(localStorage.getItem("htmx-history-cache")) || [];
        for (let e = 0; e < n.length; e++) {
            if (n[e].url === t) {
                return n[e]
            }
        }
        return null
    }

    function Ft(e) {
        const t = Q.config.requestClass;
        const n = e.cloneNode(true);
        se(m(n, "." + t), function (e) {
            o(e, t)
        });
        return n.innerHTML
    }

    function Ut() {
        const e = Dt();
        const t = S || location.pathname + location.search;
        let n;
        try {
            n = ne().querySelector('[hx-history="false" i],[data-hx-history="false" i]')
        } catch (e) {
            n = ne().querySelector('[hx-history="false"],[data-hx-history="false"]')
        }
        if (!n) {
            fe(ne().body, "htmx:beforeHistorySave", {path: t, historyElt: e});
            Xt(t, e)
        }
        if (Q.config.historyEnabled) history.replaceState({htmx: true}, ne().title, window.location.href)
    }

    function Bt(e) {
        if (Q.config.getCacheBusterParam) {
            e = e.replace(/org\.htmx\.cache-buster=[^&]*&?/, "");
            if (Y(e, "&") || Y(e, "?")) {
                e = e.slice(0, -1)
            }
        }
        if (Q.config.historyEnabled) {
            history.pushState({htmx: true}, "", e)
        }
        S = e
    }

    function Vt(e) {
        if (Q.config.historyEnabled) history.replaceState({htmx: true}, "", e);
        S = e
    }

    function jt(e) {
        se(e, function (e) {
            e.call()
        })
    }

    function _t(o) {
        const e = new XMLHttpRequest;
        const i = {path: o, xhr: e};
        fe(ne().body, "htmx:historyCacheMiss", i);
        e.open("GET", o, true);
        e.setRequestHeader("HX-Request", "true");
        e.setRequestHeader("HX-History-Restore-Request", "true");
        e.setRequestHeader("HX-Current-URL", ne().location.href);
        e.onload = function () {
            if (this.status >= 200 && this.status < 400) {
                fe(ne().body, "htmx:historyCacheMissLoad", i);
                const e = D(this.response);
                const t = e.querySelector("[hx-history-elt],[data-hx-history-elt]") || e;
                const n = Dt();
                const r = hn(n);
                Tn(e.title);
                Be(n, t, r);
                jt(r.tasks);
                S = o;
                fe(ne().body, "htmx:historyRestore", {path: o, cacheMiss: true, serverResponse: this.response})
            } else {
                ae(ne().body, "htmx:historyCacheMissLoadError", i)
            }
        };
        e.send()
    }

    function zt(e) {
        Ut();
        e = e || location.pathname + location.search;
        const t = Mt(e);
        if (t) {
            const n = D(t.content);
            const r = Dt();
            const o = hn(r);
            Tn(n.title);
            Be(r, n, o);
            jt(o.tasks);
            setTimeout(function () {
                window.scrollTo(0, t.scroll)
            }, 0);
            S = e;
            fe(ne().body, "htmx:historyRestore", {path: e, item: t})
        } else {
            if (Q.config.refreshOnHistoryMiss) {
                window.location.reload(true)
            } else {
                _t(e)
            }
        }
    }

    function $t(e) {
        let t = ve(e, "hx-indicator");
        if (t == null) {
            t = [e]
        }
        se(t, function (e) {
            const t = ie(e);
            t.requestCount = (t.requestCount || 0) + 1;
            e.classList.add.call(e.classList, Q.config.requestClass)
        });
        return t
    }

    function Jt(e) {
        let t = ve(e, "hx-disabled-elt");
        if (t == null) {
            t = []
        }
        se(t, function (e) {
            const t = ie(e);
            t.requestCount = (t.requestCount || 0) + 1;
            e.setAttribute("disabled", "")
        });
        return t
    }

    function Gt(e, t) {
        se(e, function (e) {
            const t = ie(e);
            t.requestCount = (t.requestCount || 0) - 1;
            if (t.requestCount === 0) {
                e.classList.remove.call(e.classList, Q.config.requestClass)
            }
        });
        se(t, function (e) {
            const t = ie(e);
            t.requestCount = (t.requestCount || 0) - 1;
            if (t.requestCount === 0) {
                e.removeAttribute("disabled")
            }
        })
    }

    function Kt(t, n) {
        for (let e = 0; e < t.length; e++) {
            const r = t[e];
            if (r.isSameNode(n)) {
                return true
            }
        }
        return false
    }

    function Zt(e) {
        if (e.name === "" || e.name == null || e.disabled || h(e, "fieldset[disabled]")) {
            return false
        }
        if (e.type === "button" || e.type === "submit" || e.tagName === "image" || e.tagName === "reset" || e.tagName === "file") {
            return false
        }
        if (e.type === "checkbox" || e.type === "radio") {
            return e.checked
        }
        return true
    }

    function Wt(t, e, n) {
        if (t != null && e != null) {
            if (Array.isArray(e)) {
                e.forEach(function (e) {
                    n.append(t, e)
                })
            } else {
                n.append(t, e)
            }
        }
    }

    function Yt(t, n, r) {
        if (t != null && n != null) {
            let e = r.getAll(t);
            if (Array.isArray(n)) {
                e = e.filter(e => n.indexOf(e) < 0)
            } else {
                e = e.filter(e => e !== n)
            }
            r.delete(t);
            se(e, e => r.append(t, e))
        }
    }

    function Qt(t, n, r, o, i) {
        if (o == null || Kt(t, o)) {
            return
        } else {
            t.push(o)
        }
        if (Zt(o)) {
            const s = ee(o, "name");
            let e = o.value;
            if (o.multiple && o.tagName === "SELECT") {
                e = U(o.querySelectorAll("option:checked")).map(function (e) {
                    return e.value
                })
            }
            if (o.files) {
                e = U(o.files)
            }
            Wt(s, e, n);
            if (i) {
                en(o, r)
            }
        }
        if (f(o, "form")) {
            se(o.elements, function (e) {
                if (t.indexOf(e) >= 0) {
                    Yt(e.name, e.value, n)
                } else {
                    t.push(e)
                }
                if (i) {
                    en(e, r)
                }
            });
            new FormData(o).forEach(function (e, t) {
                Wt(t, e, n)
            })
        }
    }

    function en(e, t) {
        if (e.willValidate) {
            fe(e, "htmx:validation:validate");
            if (!e.checkValidity()) {
                t.push({elt: e, message: e.validationMessage, validity: e.validity});
                fe(e, "htmx:validation:failed", {message: e.validationMessage, validity: e.validity})
            }
        }
    }

    function tn(t, e) {
        for (const n of e.keys()) {
            t.delete(n);
            e.getAll(n).forEach(function (e) {
                t.append(n, e)
            })
        }
        return t
    }

    function nn(e, t) {
        const n = [];
        const r = new FormData;
        const o = new FormData;
        const i = [];
        const s = ie(e);
        if (s.lastButtonClicked && !le(s.lastButtonClicked)) {
            s.lastButtonClicked = null
        }
        let l = f(e, "form") && e.noValidate !== true || te(e, "hx-validate") === "true";
        if (s.lastButtonClicked) {
            l = l && s.lastButtonClicked.formNoValidate !== true
        }
        if (t !== "get") {
            Qt(n, o, i, h(e, "form"), l)
        }
        Qt(n, r, i, e, l);
        if (s.lastButtonClicked || e.tagName === "BUTTON" || e.tagName === "INPUT" && ee(e, "type") === "submit") {
            const c = s.lastButtonClicked || e;
            const a = ee(c, "name");
            Wt(a, c.value, o)
        }
        const u = ve(e, "hx-include");
        se(u, function (e) {
            Qt(n, r, i, e, l);
            if (!f(e, "form")) {
                se(e.querySelectorAll(nt), function (e) {
                    Qt(n, r, i, e, l)
                })
            }
        });
        tn(r, o);
        return {errors: i, formData: r, values: Rn(r)}
    }

    function rn(e, t, n) {
        if (e !== "") {
            e += "&"
        }
        if (String(n) === "[object Object]") {
            n = JSON.stringify(n)
        }
        const r = encodeURIComponent(n);
        e += encodeURIComponent(t) + "=" + r;
        return e
    }

    function on(e) {
        e = Cn(e);
        let n = "";
        e.forEach(function (e, t) {
            n = rn(n, t, e)
        });
        return n
    }

    function sn(e, t, n) {
        const r = {
            "HX-Request": "true",
            "HX-Trigger": ee(e, "id"),
            "HX-Trigger-Name": ee(e, "name"),
            "HX-Target": te(t, "id"),
            "HX-Current-URL": ne().location.href
        };
        he(e, "hx-headers", false, r);
        if (n !== undefined) {
            r["HX-Prompt"] = n
        }
        if (ie(e).boosted) {
            r["HX-Boosted"] = "true"
        }
        return r
    }

    function ln(n, e) {
        const t = re(e, "hx-params");
        if (t) {
            if (t === "none") {
                return new FormData
            } else if (t === "*") {
                return n
            } else if (t.indexOf("not ") === 0) {
                se(t.substr(4).split(","), function (e) {
                    e = e.trim();
                    n.delete(e)
                });
                return n
            } else {
                const r = new FormData;
                se(t.split(","), function (t) {
                    t = t.trim();
                    if (n.has(t)) {
                        n.getAll(t).forEach(function (e) {
                            r.append(t, e)
                        })
                    }
                });
                return r
            }
        } else {
            return n
        }
    }

    function un(e) {
        return ee(e, "href") && ee(e, "href").indexOf("#") >= 0
    }

    function cn(e, t) {
        const n = t || re(e, "hx-swap");
        const r = {
            swapStyle: ie(e).boosted ? "innerHTML" : Q.config.defaultSwapStyle,
            swapDelay: Q.config.defaultSwapDelay,
            settleDelay: Q.config.defaultSettleDelay
        };
        if (Q.config.scrollIntoViewOnBoost && ie(e).boosted && !un(e)) {
            r.show = "top"
        }
        if (n) {
            const s = V(n);
            if (s.length > 0) {
                for (let e = 0; e < s.length; e++) {
                    const l = s[e];
                    if (l.indexOf("swap:") === 0) {
                        r.swapDelay = d(l.substr(5))
                    } else if (l.indexOf("settle:") === 0) {
                        r.settleDelay = d(l.substr(7))
                    } else if (l.indexOf("transition:") === 0) {
                        r.transition = l.substr(11) === "true"
                    } else if (l.indexOf("ignoreTitle:") === 0) {
                        r.ignoreTitle = l.substr(12) === "true"
                    } else if (l.indexOf("scroll:") === 0) {
                        const u = l.substr(7);
                        var o = u.split(":");
                        const c = o.pop();
                        var i = o.length > 0 ? o.join(":") : null;
                        r.scroll = c;
                        r.scrollTarget = i
                    } else if (l.indexOf("show:") === 0) {
                        const a = l.substr(5);
                        var o = a.split(":");
                        const f = o.pop();
                        var i = o.length > 0 ? o.join(":") : null;
                        r.show = f;
                        r.showTarget = i
                    } else if (l.indexOf("focus-scroll:") === 0) {
                        const h = l.substr("focus-scroll:".length);
                        r.focusScroll = h == "true"
                    } else if (e == 0) {
                        r.swapStyle = l
                    } else {
                        v("Unknown modifier in hx-swap: " + l)
                    }
                }
            }
        }
        return r
    }

    function an(e) {
        return re(e, "hx-encoding") === "multipart/form-data" || f(e, "form") && ee(e, "enctype") === "multipart/form-data"
    }

    function fn(t, n, r) {
        let o = null;
        kt(n, function (e) {
            if (o == null) {
                o = e.encodeParameters(t, r, n)
            }
        });
        if (o != null) {
            return o
        } else {
            if (an(n)) {
                return Cn(r)
            } else {
                return on(r)
            }
        }
    }

    function hn(e) {
        return {tasks: [], elts: [e]}
    }

    function dn(e, t) {
        const n = e[0];
        const r = e[e.length - 1];
        if (t.scroll) {
            var o = null;
            if (t.scrollTarget) {
                o = ce(n, t.scrollTarget)
            }
            if (t.scroll === "top" && (n || o)) {
                o = o || n;
                o.scrollTop = 0
            }
            if (t.scroll === "bottom" && (r || o)) {
                o = o || r;
                o.scrollTop = o.scrollHeight
            }
        }
        if (t.show) {
            var o = null;
            if (t.showTarget) {
                let e = t.showTarget;
                if (t.showTarget === "window") {
                    e = "body"
                }
                o = ce(n, e)
            }
            if (t.show === "top" && (n || o)) {
                o = o || n;
                o.scrollIntoView({block: "start", behavior: Q.config.scrollBehavior})
            }
            if (t.show === "bottom" && (r || o)) {
                o = o || r;
                o.scrollIntoView({block: "end", behavior: Q.config.scrollBehavior})
            }
        }
    }

    function he(r, e, o, i) {
        if (i == null) {
            i = {}
        }
        if (r == null) {
            return i
        }
        const s = te(r, e);
        if (s) {
            let e = s.trim();
            let t = o;
            if (e === "unset") {
                return null
            }
            if (e.indexOf("javascript:") === 0) {
                e = e.substr(11);
                t = true
            } else if (e.indexOf("js:") === 0) {
                e = e.substr(3);
                t = true
            }
            if (e.indexOf("{") !== 0) {
                e = "{" + e + "}"
            }
            let n;
            if (t) {
                n = gn(r, function () {
                    return Function("return (" + e + ")")()
                }, {})
            } else {
                n = w(e)
            }
            for (const l in n) {
                if (n.hasOwnProperty(l)) {
                    if (i[l] == null) {
                        i[l] = n[l]
                    }
                }
            }
        }
        return he(u(r), e, o, i)
    }

    function gn(e, t, n) {
        if (Q.config.allowEval) {
            return t()
        } else {
            ae(e, "htmx:evalDisallowedError");
            return n
        }
    }

    function pn(e, t) {
        return he(e, "hx-vars", true, t)
    }

    function mn(e, t) {
        return he(e, "hx-vals", false, t)
    }

    function xn(e) {
        return Cn(ue(pn(e), mn(e)))
    }

    function yn(t, n, r) {
        if (r !== null) {
            try {
                t.setRequestHeader(n, r)
            } catch (e) {
                t.setRequestHeader(n, encodeURIComponent(r));
                t.setRequestHeader(n + "-URI-AutoEncoded", "true")
            }
        }
    }

    function bn(t) {
        if (t.responseURL && typeof URL !== "undefined") {
            try {
                const e = new URL(t.responseURL);
                return e.pathname + e.search
            } catch (e) {
                ae(ne().body, "htmx:badResponseUrl", {url: t.responseURL})
            }
        }
    }

    function C(e, t) {
        return t.test(e.getAllResponseHeaders())
    }

    function vn(e, t, n) {
        e = e.toLowerCase();
        if (n) {
            if (n instanceof Element || X(n, "String")) {
                return de(e, t, null, null, {targetOverride: x(n), returnPromise: true})
            } else {
                return de(e, t, x(n.source), n.event, {
                    handler: n.handler,
                    headers: n.headers,
                    values: n.values,
                    targetOverride: x(n.target),
                    swapOverride: n.swap,
                    select: n.select,
                    returnPromise: true
                })
            }
        } else {
            return de(e, t, null, null, {returnPromise: true})
        }
    }

    function wn(e) {
        const t = [];
        while (e) {
            t.push(e);
            e = e.parentElement
        }
        return t
    }

    function Sn(e, t, n) {
        let r;
        let o;
        if (typeof URL === "function") {
            o = new URL(t, document.location.href);
            const i = document.location.origin;
            r = i === o.origin
        } else {
            o = t;
            r = l(t, document.location.origin)
        }
        if (Q.config.selfRequestsOnly) {
            if (!r) {
                return false
            }
        }
        return fe(e, "htmx:validateUrl", ue({url: o, sameHost: r}, n))
    }

    function Cn(e) {
        if (e instanceof FormData) return e;
        const t = new FormData;
        for (const n in e) {
            if (e.hasOwnProperty(n)) {
                if (typeof e[n].forEach === "function") {
                    e[n].forEach(function (e) {
                        t.append(n, e)
                    })
                } else if (typeof e[n] === "object") {
                    t.append(n, JSON.stringify(e[n]))
                } else {
                    t.append(n, e[n])
                }
            }
        }
        return t
    }

    function En(r, o, e) {
        return new Proxy(e, {
            get: function (t, e) {
                if (typeof e === "number") return t[e];
                if (e === "length") return t.length;
                if (e === "push") {
                    return function (e) {
                        t.push(e);
                        r.append(o, e)
                    }
                }
                if (typeof t[e] === "function") {
                    return function () {
                        t[e].apply(t, arguments);
                        r.delete(o);
                        t.forEach(function (e) {
                            r.append(o, e)
                        })
                    }
                }
                if (t[e] && t[e].length === 1) {
                    return t[e][0]
                } else {
                    return t[e]
                }
            }, set: function (e, t, n) {
                e[t] = n;
                r.delete(o);
                e.forEach(function (e) {
                    r.append(o, e)
                });
                return true
            }
        })
    }

    function Rn(r) {
        return new Proxy(r, {
            get: function (e, t) {
                if (typeof t === "symbol") {
                    return Reflect.get(...arguments)
                }
                if (t === "toJSON") {
                    return () => Object.fromEntries(r)
                }
                if (t in e) {
                    if (typeof e[t] === "function") {
                        return function () {
                            return r[t].apply(r, arguments)
                        }
                    } else {
                        return e[t]
                    }
                }
                const n = r.getAll(t);
                if (n.length === 0) {
                    return undefined
                } else if (n.length === 1) {
                    return n[0]
                } else {
                    return En(e, t, n)
                }
            }, set: function (t, n, e) {
                t.delete(n);
                if (typeof e.forEach === "function") {
                    e.forEach(function (e) {
                        t.append(n, e)
                    })
                } else {
                    t.append(n, e)
                }
                return true
            }, deleteProperty: function (e, t) {
                e.delete(t);
                return true
            }, ownKeys: function (e) {
                return Reflect.ownKeys(Object.fromEntries(e))
            }, getOwnPropertyDescriptor: function (e, t) {
                return Reflect.getOwnPropertyDescriptor(Object.fromEntries(e), t)
            }
        })
    }

    function de(t, n, r, o, i, D) {
        let s = null;
        let l = null;
        i = i != null ? i : {};
        if (i.returnPromise && typeof Promise !== "undefined") {
            var e = new Promise(function (e, t) {
                s = e;
                l = t
            })
        }
        if (r == null) {
            r = ne().body
        }
        const X = i.handler || Nn;
        const M = i.select || null;
        if (!le(r)) {
            oe(s);
            return e
        }
        const u = i.targetOverride || Se(r);
        if (u == null || u == be) {
            ae(r, "htmx:targetError", {target: te(r, "hx-target")});
            oe(l);
            return e
        }
        let c = ie(r);
        const a = c.lastButtonClicked;
        if (a) {
            const N = ee(a, "formaction");
            if (N != null) {
                n = N
            }
            const L = ee(a, "formmethod");
            if (L != null) {
                if (L.toLowerCase() !== "dialog") {
                    t = L
                }
            }
        }
        const f = re(r, "hx-confirm");
        if (D === undefined) {
            const G = function (e) {
                return de(t, n, r, o, i, !!e)
            };
            const K = {target: u, elt: r, path: n, verb: t, triggeringEvent: o, etc: i, issueRequest: G, question: f};
            if (fe(r, "htmx:confirm", K) === false) {
                oe(s);
                return e
            }
        }
        let h = r;
        let d = re(r, "hx-sync");
        let g = null;
        let F = false;
        if (d) {
            const A = d.split(":");
            const I = A[0].trim();
            if (I === "this") {
                h = we(r, "hx-sync")
            } else {
                h = ce(r, I)
            }
            d = (A[1] || "drop").trim();
            c = ie(h);
            if (d === "drop" && c.xhr && c.abortable !== true) {
                oe(s);
                return e
            } else if (d === "abort") {
                if (c.xhr) {
                    oe(s);
                    return e
                } else {
                    F = true
                }
            } else if (d === "replace") {
                fe(h, "htmx:abort")
            } else if (d.indexOf("queue") === 0) {
                const Z = d.split(" ");
                g = (Z[1] || "last").trim()
            }
        }
        if (c.xhr) {
            if (c.abortable) {
                fe(h, "htmx:abort")
            } else {
                if (g == null) {
                    if (o) {
                        const P = ie(o);
                        if (P && P.triggerSpec && P.triggerSpec.queue) {
                            g = P.triggerSpec.queue
                        }
                    }
                    if (g == null) {
                        g = "last"
                    }
                }
                if (c.queuedRequests == null) {
                    c.queuedRequests = []
                }
                if (g === "first" && c.queuedRequests.length === 0) {
                    c.queuedRequests.push(function () {
                        de(t, n, r, o, i)
                    })
                } else if (g === "all") {
                    c.queuedRequests.push(function () {
                        de(t, n, r, o, i)
                    })
                } else if (g === "last") {
                    c.queuedRequests = [];
                    c.queuedRequests.push(function () {
                        de(t, n, r, o, i)
                    })
                }
                oe(s);
                return e
            }
        }
        const p = new XMLHttpRequest;
        c.xhr = p;
        c.abortable = F;
        const m = function () {
            c.xhr = null;
            c.abortable = false;
            if (c.queuedRequests != null && c.queuedRequests.length > 0) {
                const e = c.queuedRequests.shift();
                e()
            }
        };
        const U = re(r, "hx-prompt");
        if (U) {
            var x = prompt(U);
            if (x === null || !fe(r, "htmx:prompt", {prompt: x, target: u})) {
                oe(s);
                m();
                return e
            }
        }
        if (f && !D) {
            if (!confirm(f)) {
                oe(s);
                m();
                return e
            }
        }
        let y = sn(r, u, x);
        if (t !== "get" && !an(r)) {
            y["Content-Type"] = "application/x-www-form-urlencoded"
        }
        if (i.headers) {
            y = ue(y, i.headers)
        }
        const B = nn(r, t);
        let b = B.errors;
        const V = B.formData;
        if (i.values) {
            tn(V, Cn(i.values))
        }
        const j = xn(r);
        const v = tn(V, j);
        let w = ln(v, r);
        if (Q.config.getCacheBusterParam && t === "get") {
            w.set("org.htmx.cache-buster", ee(u, "id") || "true")
        }
        if (n == null || n === "") {
            n = ne().location.href
        }
        const S = he(r, "hx-request");
        const _ = ie(r).boosted;
        let C = Q.config.methodsThatUseUrlParams.indexOf(t) >= 0;
        const E = {
            boosted: _,
            useUrlParams: C,
            formData: w,
            parameters: Rn(w),
            unfilteredFormData: v,
            unfilteredParameters: Rn(v),
            headers: y,
            target: u,
            verb: t,
            errors: b,
            withCredentials: i.credentials || S.credentials || Q.config.withCredentials,
            timeout: i.timeout || S.timeout || Q.config.timeout,
            path: n,
            triggeringEvent: o
        };
        if (!fe(r, "htmx:configRequest", E)) {
            oe(s);
            m();
            return e
        }
        n = E.path;
        t = E.verb;
        y = E.headers;
        w = Cn(E.parameters);
        b = E.errors;
        C = E.useUrlParams;
        if (b && b.length > 0) {
            fe(r, "htmx:validation:halted", E);
            oe(s);
            m();
            return e
        }
        const z = n.split("#");
        const $ = z[0];
        const R = z[1];
        let O = n;
        if (C) {
            O = $;
            const W = !w.keys().next().done;
            if (W) {
                if (O.indexOf("?") < 0) {
                    O += "?"
                } else {
                    O += "&"
                }
                O += on(w);
                if (R) {
                    O += "#" + R
                }
            }
        }
        if (!Sn(r, O, E)) {
            ae(r, "htmx:invalidPath", E);
            oe(l);
            return e
        }
        p.open(t.toUpperCase(), O, true);
        p.overrideMimeType("text/html");
        p.withCredentials = E.withCredentials;
        p.timeout = E.timeout;
        if (S.noHeaders) {
        } else {
            for (const k in y) {
                if (y.hasOwnProperty(k)) {
                    const Y = y[k];
                    yn(p, k, Y)
                }
            }
        }
        const H = {
            xhr: p,
            target: u,
            requestConfig: E,
            etc: i,
            boosted: _,
            select: M,
            pathInfo: {requestPath: n, finalRequestPath: O, anchor: R}
        };
        p.onload = function () {
            try {
                const t = wn(r);
                H.pathInfo.responsePath = bn(p);
                X(r, H);
                Gt(q, T);
                fe(r, "htmx:afterRequest", H);
                fe(r, "htmx:afterOnLoad", H);
                if (!le(r)) {
                    let e = null;
                    while (t.length > 0 && e == null) {
                        const n = t.shift();
                        if (le(n)) {
                            e = n
                        }
                    }
                    if (e) {
                        fe(e, "htmx:afterRequest", H);
                        fe(e, "htmx:afterOnLoad", H)
                    }
                }
                oe(s);
                m()
            } catch (e) {
                ae(r, "htmx:onLoadError", ue({error: e}, H));
                throw e
            }
        };
        p.onerror = function () {
            Gt(q, T);
            ae(r, "htmx:afterRequest", H);
            ae(r, "htmx:sendError", H);
            oe(l);
            m()
        };
        p.onabort = function () {
            Gt(q, T);
            ae(r, "htmx:afterRequest", H);
            ae(r, "htmx:sendAbort", H);
            oe(l);
            m()
        };
        p.ontimeout = function () {
            Gt(q, T);
            ae(r, "htmx:afterRequest", H);
            ae(r, "htmx:timeout", H);
            oe(l);
            m()
        };
        if (!fe(r, "htmx:beforeRequest", H)) {
            oe(s);
            m();
            return e
        }
        var q = $t(r);
        var T = Jt(r);
        se(["loadstart", "loadend", "progress", "abort"], function (t) {
            se([p, p.upload], function (e) {
                e.addEventListener(t, function (e) {
                    fe(r, "htmx:xhr:" + t, {lengthComputable: e.lengthComputable, loaded: e.loaded, total: e.total})
                })
            })
        });
        fe(r, "htmx:beforeSend", H);
        const J = C ? null : fn(p, r, w);
        p.send(J);
        return e
    }

    function On(e, t) {
        const n = t.xhr;
        let r = null;
        let o = null;
        if (C(n, /HX-Push:/i)) {
            r = n.getResponseHeader("HX-Push");
            o = "push"
        } else if (C(n, /HX-Push-Url:/i)) {
            r = n.getResponseHeader("HX-Push-Url");
            o = "push"
        } else if (C(n, /HX-Replace-Url:/i)) {
            r = n.getResponseHeader("HX-Replace-Url");
            o = "replace"
        }
        if (r) {
            if (r === "false") {
                return {}
            } else {
                return {type: o, path: r}
            }
        }
        const i = t.pathInfo.finalRequestPath;
        const s = t.pathInfo.responsePath;
        const l = re(e, "hx-push-url");
        const u = re(e, "hx-replace-url");
        const c = ie(e).boosted;
        let a = null;
        let f = null;
        if (l) {
            a = "push";
            f = l
        } else if (u) {
            a = "replace";
            f = u
        } else if (c) {
            a = "push";
            f = s || i
        }
        if (f) {
            if (f === "false") {
                return {}
            }
            if (f === "true") {
                f = s || i
            }
            if (t.pathInfo.anchor && f.indexOf("#") === -1) {
                f = f + "#" + t.pathInfo.anchor
            }
            return {type: a, path: f}
        } else {
            return {}
        }
    }

    function Hn(e, t) {
        var n = new RegExp(e.code);
        return n.test(t)
    }

    function qn(e) {
        for (var t = 0; t < Q.config.responseHandling.length; t++) {
            var n = Q.config.responseHandling[t];
            if (Hn(n, e.status)) {
                return n
            }
        }
        return {swap: false}
    }

    function Tn(e) {
        if (e) {
            const t = r("title");
            if (t) {
                t.innerHTML = e
            } else {
                window.document.title = e
            }
        }
    }

    function Nn(o, i) {
        const s = i.xhr;
        let l = i.target;
        const e = i.etc;
        const u = i.select;
        if (!fe(o, "htmx:beforeOnLoad", i)) return;
        if (C(s, /HX-Trigger:/i)) {
            ze(s, "HX-Trigger", o)
        }
        if (C(s, /HX-Location:/i)) {
            Ut();
            let e = s.getResponseHeader("HX-Location");
            var c;
            if (e.indexOf("{") === 0) {
                c = w(e);
                e = c.path;
                delete c.path
            }
            vn("GET", e, c).then(function () {
                Bt(e)
            });
            return
        }
        const t = C(s, /HX-Refresh:/i) && s.getResponseHeader("HX-Refresh") === "true";
        if (C(s, /HX-Redirect:/i)) {
            location.href = s.getResponseHeader("HX-Redirect");
            t && location.reload();
            return
        }
        if (t) {
            location.reload();
            return
        }
        if (C(s, /HX-Retarget:/i)) {
            if (s.getResponseHeader("HX-Retarget") === "this") {
                i.target = o
            } else {
                i.target = ce(o, s.getResponseHeader("HX-Retarget"))
            }
        }
        const a = On(o, i);
        const n = qn(s);
        const r = n.swap;
        let f = !!n.error;
        let h = Q.config.ignoreTitle || n.ignoreTitle;
        let d = n.select;
        if (n.target) {
            i.target = ce(o, n.target)
        }
        var g = e.swapOverride;
        if (g == null && n.swapOverride) {
            g = n.swapOverride
        }
        if (C(s, /HX-Retarget:/i)) {
            if (s.getResponseHeader("HX-Retarget") === "this") {
                i.target = o
            } else {
                i.target = ce(o, s.getResponseHeader("HX-Retarget"))
            }
        }
        if (C(s, /HX-Reswap:/i)) {
            g = s.getResponseHeader("HX-Reswap")
        }
        var p = s.response;
        var m = ue({shouldSwap: r, serverResponse: p, isError: f, ignoreTitle: h, selectOverride: d}, i);
        if (n.event && !fe(l, n.event, m)) return;
        if (!fe(l, "htmx:beforeSwap", m)) return;
        l = m.target;
        p = m.serverResponse;
        f = m.isError;
        h = m.ignoreTitle;
        d = m.selectOverride;
        i.target = l;
        i.failed = f;
        i.successful = !f;
        if (m.shouldSwap) {
            if (s.status === 286) {
                it(o)
            }
            kt(o, function (e) {
                p = e.transformResponse(p, s, o)
            });
            if (a.type) {
                Ut()
            }
            if (C(s, /HX-Reswap:/i)) {
                g = s.getResponseHeader("HX-Reswap")
            }
            var c = cn(o, g);
            if (!c.hasOwnProperty("ignoreTitle")) {
                c.ignoreTitle = h
            }
            l.classList.add(Q.config.swappingClass);
            let n = null;
            let r = null;
            if (u) {
                d = u
            }
            if (C(s, /HX-Reselect:/i)) {
                d = s.getResponseHeader("HX-Reselect")
            }
            const x = re(o, "hx-select-oob");
            const y = re(o, "hx-select");
            let e = function () {
                try {
                    if (a.type) {
                        fe(ne().body, "htmx:beforeHistoryUpdate", ue({history: a}, i));
                        if (a.type === "push") {
                            Bt(a.path);
                            fe(ne().body, "htmx:pushedIntoHistory", {path: a.path})
                        } else {
                            Vt(a.path);
                            fe(ne().body, "htmx:replacedInHistory", {path: a.path})
                        }
                    }
                    _e(l, p, c, {
                        select: d || y,
                        selectOOB: x,
                        eventInfo: i,
                        anchor: i.pathInfo.anchor,
                        contextElement: o,
                        afterSwapCallback: function () {
                            if (C(s, /HX-Trigger-After-Swap:/i)) {
                                let e = o;
                                if (!le(o)) {
                                    e = ne().body
                                }
                                ze(s, "HX-Trigger-After-Swap", e)
                            }
                        },
                        afterSettleCallback: function () {
                            if (C(s, /HX-Trigger-After-Settle:/i)) {
                                let e = o;
                                if (!le(o)) {
                                    e = ne().body
                                }
                                ze(s, "HX-Trigger-After-Settle", e)
                            }
                            oe(n)
                        }
                    })
                } catch (e) {
                    ae(o, "htmx:swapError", i);
                    oe(r);
                    throw e
                }
            };
            let t = Q.config.globalViewTransitions;
            if (c.hasOwnProperty("transition")) {
                t = c.transition
            }
            if (t && fe(o, "htmx:beforeTransition", i) && typeof Promise !== "undefined" && document.startViewTransition) {
                const b = new Promise(function (e, t) {
                    n = e;
                    r = t
                });
                const v = e;
                e = function () {
                    document.startViewTransition(function () {
                        v();
                        return b
                    })
                }
            }
            if (c.swapDelay > 0) {
                setTimeout(e, c.swapDelay)
            } else {
                e()
            }
        }
        if (f) {
            ae(o, "htmx:responseError", ue({error: "Response Status Error Code " + s.status + " from " + i.pathInfo.requestPath}, i))
        }
    }

    const Ln = {};

    function An() {
        return {
            init: function (e) {
                return null
            }, onEvent: function (e, t) {
                return true
            }, transformResponse: function (e, t, n) {
                return e
            }, isInlineSwap: function (e) {
                return false
            }, handleSwap: function (e, t, n, r) {
                return false
            }, encodeParameters: function (e, t, n) {
                return null
            }
        }
    }

    function In(e, t) {
        if (t.init) {
            t.init(n)
        }
        Ln[e] = ue(An(), t)
    }

    function Pn(e) {
        delete Ln[e]
    }

    function kn(e, n, r) {
        if (n == undefined) {
            n = []
        }
        if (e == undefined) {
            return n
        }
        if (r == undefined) {
            r = []
        }
        const t = te(e, "hx-ext");
        if (t) {
            se(t.split(","), function (e) {
                e = e.replace(/ /g, "");
                if (e.slice(0, 7) == "ignore:") {
                    r.push(e.slice(7));
                    return
                }
                if (r.indexOf(e) < 0) {
                    const t = Ln[e];
                    if (t && n.indexOf(t) < 0) {
                        n.push(t)
                    }
                }
            })
        }
        return kn(u(e), n, r)
    }

    var Dn = false;
    ne().addEventListener("DOMContentLoaded", function () {
        Dn = true
    });

    function Xn(e) {
        if (Dn || ne().readyState === "complete") {
            e()
        } else {
            ne().addEventListener("DOMContentLoaded", e)
        }
    }

    function Mn() {
        if (Q.config.includeIndicatorStyles !== false) {
            ne().head.insertAdjacentHTML("beforeend", "<style>      ." + Q.config.indicatorClass + "{opacity:0}      ." + Q.config.requestClass + " ." + Q.config.indicatorClass + "{opacity:1; transition: opacity 200ms ease-in;}      ." + Q.config.requestClass + "." + Q.config.indicatorClass + "{opacity:1; transition: opacity 200ms ease-in;}      </style>")
        }
    }

    function Fn() {
        const e = ne().querySelector('meta[name="htmx-config"]');
        if (e) {
            return w(e.content)
        } else {
            return null
        }
    }

    function Un() {
        const e = Fn();
        if (e) {
            Q.config = ue(Q.config, e)
        }
    }

    Xn(function () {
        Un();
        Mn();
        let e = ne().body;
        Lt(e);
        const t = ne().querySelectorAll("[hx-trigger='restored'],[data-hx-trigger='restored']");
        e.addEventListener("htmx:abort", function (e) {
            const t = e.target;
            const n = ie(t);
            if (n && n.xhr) {
                n.xhr.abort()
            }
        });
        const n = window.onpopstate ? window.onpopstate.bind(window) : null;
        window.onpopstate = function (e) {
            if (e.state && e.state.htmx) {
                zt();
                se(t, function (e) {
                    fe(e, "htmx:restored", {document: ne(), triggerEvent: fe})
                })
            } else {
                if (n) {
                    n(e)
                }
            }
        };
        setTimeout(function () {
            fe(e, "htmx:load", {});
            e = null
        }, 0)
    });
    return Q
}();