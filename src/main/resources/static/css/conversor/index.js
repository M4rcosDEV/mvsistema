import fs from "fs";

const map = {
  "background": "-fx-background-color",
  "background-color": "-fx-background-color",
  "background-image": "-fx-background-image",
  "background-size": "-fx-background-size",
  "background-repeat": "-fx-background-repeat",
  "background-position": "-fx-background-position",

  "color": "-fx-text-fill",
  "font-size": "-fx-font-size",
  "font-family": "-fx-font-family",
  "font-weight": "-fx-font-weight",
  "font-style": "-fx-font-style",

  "border": "-fx-border-color",
  "border-color": "-fx-border-color",
  "border-width": "-fx-border-width",
  "border-style": "-fx-border-style",
  "border-radius": "-fx-border-radius",

  "padding": "-fx-padding",
  "margin": "",

  "text-align": "-fx-text-alignment",
  "text-decoration": "-fx-underline",
  "line-height": "-fx-line-spacing",

  "width": "-fx-pref-width",
  "height": "-fx-pref-height",
  "min-width": "-fx-min-width",
  "min-height": "-fx-min-height",
  "max-width": "-fx-max-width",
  "max-height": "-fx-max-height",

  "opacity": "-fx-opacity",
  "cursor": "-fx-cursor",
  "visibility": "-fx-visible",
  "display": "",

  "box-shadow": "-fx-effect",
  "text-shadow": "-fx-effect",

  "border-top-color": "-fx-border-color",
  "border-right-color": "-fx-border-color",
  "border-bottom-color": "-fx-border-color",
  "border-left-color": "-fx-border-color",

  "border-top-width": "-fx-border-width",
  "border-right-width": "-fx-border-width",
  "border-bottom-width": "-fx-border-width",
  "border-left-width": "-fx-border-width",

  "border-top-left-radius": "-fx-border-radius",
  "border-top-right-radius": "-fx-border-radius",
  "border-bottom-left-radius": "-fx-border-radius",
  "border-bottom-right-radius": "-fx-border-radius",

  "outline": "",
  "outline-color": "",
  "outline-width": "",
  "outline-style": "",

  "overflow": "",
  "overflow-x": "",
  "overflow-y": "",

  "transform": "",
  "transition": "",
  "animation": "",

  "justify-content": "",
  "align-items": "",
  "align-content": "",

  "flex": "",
  "flex-direction": "",
  "flex-wrap": "",
  "flex-grow": "",
  "flex-shrink": "",

  "position": "",
  "top": "",
  "right": "",
  "bottom": "",
  "left": "",

  "z-index": "",
  "box-sizing": "",

  "letter-spacing": "-fx-letter-spacing",
  "word-spacing": "-fx-word-spacing",

  "list-style": "-fx-list-style",
  "list-style-type": "-fx-list-style-type",

  "content": "-fx-content",
  "clip-path": "-fx-clip",

  "transition-duration": "",
  "transition-property": "",

  "background-clip": "-fx-background-insets",
  "border-inset": "-fx-border-insets"
}

const css = fs.readFileSync("input.css", "utf-8");

const converted = css.replace(
  /([a-z-]+)\s*:/g,
  (_, prop) => (map[prop] ? map[prop] + ":" : prop + ":")
);

fs.writeFileSync("output.css", converted);
console.log("✅ CSS convertido para CSSFX!");