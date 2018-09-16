# RevealJS-Template 2016

Auf der Grünen Wiese arbeiten wir doch alle am liebsten

![Twitter - Holisticon](images/brand/tweet_logo.png) <!-- .element: class="inline" style="max-height:32px; vertical-align: top; " --> [@holisticon](https://twitter.com/holisticon)<br />

<!-- .slide: class="title" data-background="images/brand/grass_footer.png" data-background-repeat="repeat-x" data-background-position="bottom center" data-background-size="inherit" -->

---

# Bedienung

* Slides durschalten: Pfeiltasten oder Space
* Fullscreen: `f` oder `F11`
* Präsentation ausblenden: `b`
* Präsentation durchgehen: `n`(ext), `p`(revious)
* Folien-Übersicht: `ESC`
* Präsentationsmodus: `s`

--

# Folien bearbeiten


Its markdown!

Einfach diese Datei bearbeiten:

`slides/slides.md`


Diese Präsentation starten:

```
/holisticon-template $ grunt
```

--

# Präsentationsnotizen

Die Notizen müssen am Ende einer Slide stehen!

```no-highlight
 >NOTES:
 * Alles bis zum folgenden Seitenumbruch landet in den Speakers-Notes.
 * Einfach mit der Taste `s` ausprobieren!
```

>NOTES:
* Alles bis zum folgenden Seitenumbruch landet in den Speakers-Notes.
* Einfach mit der Taste `s` ausprobieren!
* Präsentation im Browser mit dem URL-Suffix `?print-pdf#/1` starten und mittels Print-Dialog als PDF abspeichern.
Klappt am hübschesten im Chrome.

---

# Text

Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus <br>est Lorem ipsum dolor sit amet.

--

# Regeln

Maßvoller Umgang mit Text, weniger ist mehr!

--

# Source


```no-highlight
# Text

Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam
nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam
erat, sed diam voluptua. At vero eos et accusam et justo duo dolores
et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est
<br>Lorem ipsum dolor sit amet.
```

---

# Bullet Points
* Lorem
* ipsum
* dolor

--

# Regeln

* Sparsam mit Text
* Möglichst kein Text über mehrere Zeilen
* Nur eine Ebene von Bullet Points

--

# Source

```no-highlight
# Bullet Points
* Lorem
* ipsum
* dolor

```


---

# Aufzählung

Vor Aufzählungen oder auch Bullets Points <br>kann ein einleitender Text stehen

1. Erster Punkt
2. Zweiter Punkt
3. 42

--

# Regeln

* Sparsam mit Text
* Möglichst kein Text über mehrere Zeilen
* Nur eine Ebene von Aufzählungen

--

# Source

```no-highlight
# Aufzählung

Vor Aufzählungen oder auch Bullets Points <br>kann ein einleitender Text stehen

1. Erster Punkt
2. Zweiter Punkt
3. 42
```

---

# Formatierung: Hervorhebungen

Um im Text Hervorhebungen vorzunehmen kann <br>Schrift *kursiv*, **fett** oder auch ***fett und kursiv*** gesetzt werden.
<br>Ebenso ist `Monospace` erlaubt.


--

# Regeln

Sparsam mit Hervorhebungen <br>(sonst ***würgen*** sie nicht mehr)

--

# Source

```no-highlight
# Formatierung: Hervorhebungen

Um im Text Hervorhebungen vorzunehmen kann <br>
Schrift *kursiv*, **fett** oder auch
***fett und kursiv*** gesetzt werden.
<br>Ebenso ist `Monospace` erlaubt.

```


---

# Formatierung: iFrame

Um einen iFrame bildschirmfüllend zu nutzen kann die CSS-Klasse "stretch" genutzt werden

<div class="stretch ">
	<iframe src="http://heise.de" width="100%" height="100%"/>
</div>



--

# Regeln

Nur Inhalte einbinden, die erlaubt sind, z.B. von Kunden freigegeben

> Funktioniert auch in Codeblöcken <br>
> kann auch mit "fragment" kombiniert werden

--

# Source

```no-highlight
<div class="stretch">
	<iframe src="http://heise.de" width="100%" height="100%"/>
</div>

```

---

#  Formatierung: Zitate

> Alles, was nach **Ordnung**, **Maß** und **Gesetz** geschieht, erzeugt Gutes.
> <br> Das Untergeordnete und schlecht Eingeleitete ist dagegen
> <br>an sich schädlich und löset auch das Wohlgeordnete auf.

--

# Regeln

* Nur ein Zitat pro Seite
* Einleitender Text davor möglich
* Keine Absätze (zerschießt Layout!)
* Stattdessen ```<br>``` nutzen

--

# Source

```no-highlight
#  Formatierung: Zitate

> Alles, was nach **Ordnung**, **Maß** und **Gesetz** geschieht, erzeugt Gutes.
> <br> Das Untergeordnete und schlecht Eingeleitete ist dagegen
> <br>an sich schädlich und löset auch das Wohlgeordnete auf.

```

---

# Formatierung: Quellcode

```java
@Annotation
public class Foo {
    /**
     * Javadoc
     */
    public String argh = "okay";
    public char s = '\n';
    // good idea?
    /* who knows */
}
```

--

# Regeln

* Mehrere separate Code-Blöcke möglich
* Text davor, dazwischen oder danach möglich
* Einrückungen durch Leerzeichen

--

# Unterstützte Sprachen

[https://github.com/isagalaev/highlight.js/tree/master/src/languages](https://github.com/isagalaev/highlight.js/tree/master/src/languages)

--

# Java

```java
@Annotation
public class Foo {
    /**
     * Javadoc
     */
    public String argh = "okay";
    public char s = '\n';
    // good idea?
    /* who knows */
}
```


--

# Scala

```scala
/***
 * So awesome
 */
@specialized
case class Foo[T+, K-] (foo:String, bar:Future[MonadicOps[String]]) {
    @tailrec def bar() = s"soo $foo" + bar
}
```

--

# .+ML

HTML

```html
<!doctype html>
<html lang="en">
    <head></head>
    <body></body>
</html>
```
XML

```
<bars xml-ns="http://awesome.xml">
    <awesomeness with="parameters">
    </awesomeness>
</bars>
```

--

# Source


    ```java
    @Annotation
    public class Foo {
        /**
         * Javadoc
         */
        public String argh = "okay";
        public char s = '\n';
        // good idea?
        /* who knows */
    }
    ```

--


# Highlightung abschalten


`no-highlight` um Highlighting abzuschalten

<pre><code class="no-highlight">```no-highlight
@Annotation
public class Foo {
    /**
     * Javadoc
     */
    public String argh = "okay";
    public char s = '\n';
    // good idea?
    /* who knows */
}
```</code></pre>

---

# Tabellen


|  Tables       | Are           | Cool          |
|:--------------|:-------------:|--------------:|
| left align    | centered      | right aligned |
| they can      | present       |         funny |
| zebra stripes | are neat      |            $1 |


--

# Regeln


* Sparsam mit Text
* Ausrichtung nicht mischen

--

# Source

```no-highlight
| Tables        | Are           | Cool          |
|:--------------|:-------------:|--------------:|
| left align    | centered      | right aligned |
| they can      | present       |         funny |
| zebra stripes | are neat      |            $1 |
```

---

# Bilder - freigestellt

Bilder werden irgendwo im Ordner `images` abgelegt.

![img](images/wet_floor.jpg) <!-- .element: style="height:300px" -->


--

# Regeln

* Headline je nach Bild
* keine Bilder mit Hintergrund

--

# Source

Bilder innerhalb der Slide werden mit Pixeln skaliert

```no-highlight
# Bilder - freigestellt

Bilder werden irgendwo im Ordner `images` abgelegt.

![img](images/wet_floor.jpg) <!-- .element: style="height:300px" -->
```

---

# Bilder - im Anschnitt

Bilder können im Anschnitt unten positioniert werden

<br><br><br><br><br><br><br><br><br>
<!-- .slide: data-background="images/elbphilharmonie.png"  data-background-size="40%"  data-state="img-bottom"  -->

--

# Regeln

* Headline je nach Bild
* Keine rechteckigen Bilder oder Grafiken mit Hintergrund ohne Anschnitt frei auf der Folie platzieren
* Kein urheberrechtlich geschütztes Material verwenden bzw.mit Quelle und entsprechendem Copyright kennzeichnen
* durch `<br>` kann der Textblock vertikal platziert werden
* Background-Größe wird prozentual angegeben


--

# Source

```no-highlight
# Bilder - im Anschnitt

Bilder können im Anschnitt unten positioniert werden

<br><br><br><br><br><br><br><br><br>

<!-- .slide: data-background="images/wet_floor.jpg"
             data-background-size="50%"
             data-state="img-bottom"  -->
```

---


> Vollflächige Bilder können mit Zitat bzw. Statement<br>
> in einem halbtransparenten Textblock überlagert werden

<br><br><br><br><br><br>

<!-- .slide: data-background="images/baustelle.jpg" data-state="img-full" -->

--

# Regeln

* Kein urheberrechtlich geschütztes Material verwenden bzw.mit Quelle und entsprechendem Copyright kennzeichnen
* durch `<br>` kann der Textblock verttkal platziert werden



--

# Source

```no-highlight
> Vollflächige Bilder können mit Zitat bzw. Statement<br>
> in einem halbtransparenten Textblock überlagert werden

<br><br><br><br><br><br>

<!-- .slide: data-background="images/baustelle.jpg" data-state="img-full" -->
```

---

# Seitenübergänge

* Seitenübergänge werden durch *---* festgelegt
* Übergangseffekt wird global definiert in index.html
* Erlaubt sind: default, cube, page, linear, fade, none

--

# Regeln

* nur erlaubte Übergangseffekte benutzen

--

# Source

Transition einstellen

```no-highlight
transition: Reveal.getQueryHash().transition || 'default', // default/cube/page/linear/fade/none
```

Seitenübergang einfügen


```no-highlight
(Leerzeile)
---
(Leerzeile)
```

---

# Fragmente

Einzelne Blöcke können nacheinander eingeblendet werden

* Wer A sagt <!-- .element: class="fragment" -->
* muss auch B sagen! <!-- .element: class="fragment" -->

--

# Source

```no-highlight
# Fragmente

Einzelne Blöcke können nacheinander eingeblendet werden

* Wer A sagt <!-- .element: class="fragment" -->
* muss auch B sagen! <!-- .element: class="fragment" -->
```

---

# Navigation

Idee: Der _Standardpfad_ durch eine Präsentation ist horizontal.

Optionale Detailinformationen (ausführliche Grafiken, Code-Beispiele) können durch vertikale Slides an eine Standard-Slide
angehängt und dann ___bei Bedarf___ gezeigt werden.

--

Es geht Abwärts!


--

Unten angekommen

--

# Source

```no-highlight
Seite oben
(Leerzeile)
--
(Leerzeile)
Seite unten
```

---

# Last but not least


