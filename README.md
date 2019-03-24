# STILL WORK IN PROGRESS

# mkvtoolnix-wrapper
An easy to use light kotlin-jvm wrapper for most common mkvmerge and mkvpropedit CLI commands.

```
WARNING! This library is not feature complete: there are quite a few advanced options that are not currently supported.
```

The library is published on maven central:
```
compile 'com.github.mmauro94:mkvtoolnix-wrapper:1.0-SNAPSHOT'
```
# Usage


To identify a file:
```koltin
MkvToolnix.identify(File("input.mkv"))
```

To edit properties:
```kotlin
MkvPropEditCommand(File("input.mkv"))
    .editTrackPropertiesByNumber(2) {
        set("prop", "val")
        delete("anotherprop")
    }
    .executeAndPrint()
```

To run merge files:
```kotlin
MkvMergeCommand(File("output.mkv"))
    .globalOptions { 
        this.title = "Title"
    }
    .addInputFile(File("inputA.mkv")) {
        videoTracks.include {
            addById(0)
        }
    }
    .addInputFile(File("inputB.mkv")) {
        videoTracks.excludeAll()
        subtitleTracks.excludeAll()
    }
    .executeAndPrint()
```

You can find more examples [here](https://github.com/MMauro94/mkvtoolnix-wrapper/tree/master/src/main/kotlin/com/github/mmauro94/mkvtoolnix_wrapper/examples).

Methods and classes are thoroughly documented and linked to official doc when possible, so you can read more there.

# Features
As you can see from the list below, there are still some features that are WIP.
* mkvpropedit:
    - [x] Fast/full parse mode
    - [x] Edit properties
    - [x] Attachment support
    - [ ] Tag handling
* mkvmerge:
    - For each input file:
        - [x] Include/exclude specific tracks
    - For each input track:
        - [x] Change name and language
        - [x] Change forced/default flags
        - [x] Set offset/linear regression
    - [x] Change tracks order
    - [x] Identifying a file
    - [ ] Segment info handling
    - [ ] Chapter and tag handling
    - [ ] File splitting, linking, appending and concatenation
    - [ ] Attachment support
    - [ ] Advanced track specific options

# Credits
- Thanks to [jell.yfish.us](http://jell.yfish.us) for test video
- [mkvmerge documentation](https://mkvtoolnix.download/doc/mkvmerge.html)
- [mkvpropedit documentation](https://mkvtoolnix.download/doc/mkvpropedit.html)