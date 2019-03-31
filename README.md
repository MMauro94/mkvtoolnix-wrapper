# mkvtoolnix-wrapper
An easy to use light kotlin-jvm wrapper for most common mkvmerge and mkvpropedit CLI commands.

The library is published on maven central:
```
compile 'com.github.mmauro94:mkvtoolnix-wrapper:1.0.0'
```
**WARNING** This library is not feature complete: there are quite a few advanced options that are not currently supported. You can achieve them adding custom args to the command.

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

To merge files:
```kotlin
MkvMergeCommand(File("output.mkv"))
    .globalOptions { 
        title = "Title"
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

You can find more complete examples [here](https://github.com/MMauro94/mkvtoolnix-wrapper/tree/master/src/main/kotlin/com/github/mmauro94/mkvtoolnix_wrapper/examples).

The commands will use `mkvmerge` and `mkvpropedit` from the PATH environment. To change that, you can set the location of the binaries with:
```kotlin
//Set custom path
MkvToolnix.mkvToolnixPath = File("custom/path/to/binaries")

//Reset to using PATH environment
MkvToolnix.mkvToolnixPath = null
``` 

Methods and classes are thoroughly documented and linked to official doc when possible, so you can read more there.

# Supported features
As you can see from the list below, there are still some features that are not supported. You can, however, with the `additionalArgs` property, pass custom parameters to the command. 
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
