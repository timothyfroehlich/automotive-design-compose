/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.designcompose.annotation

/**
 * Generate an interface that contains functions to render various nodes in a Figma document
 *
 * @param id the id of the Figma document. This can be found in the url, e.g. figma.com/file/<id>
 * @param designVersion version id of the Figma document.
 * @param customizationInterfaceVersion a version string that gets written to a generated JSON file
 *   describing the customization interface which is used for the Design Compose Figma plugin.
 */
@Target(AnnotationTarget.CLASS)
annotation class DesignDoc(
    val id: String,
    val designVersion: String = "",
    val customizationInterfaceVersion: String = "0",
    val designFeatures: Array<String> = [], // Allowed values: "shader"
)

/**
 * Generate a @Composable function that renders the given node
 *
 * @param node the name of the Figma node
 * @param hideDesignSwitcher set to true if this is a root node and you do not want to show the
 *   design switcher. Defaulted to false
 * @param isRoot set to true if this is the root node. All customizations should be set in a root
 *   node to be passed down to child nodes. Defaulted to false. This is used in the generated JSON
 *   file used for the Design Compose Figma plugin
 */
@Target(AnnotationTarget.FUNCTION)
annotation class DesignComponent(
    val node: String,
    val hideDesignSwitcher: Boolean = false,
    val isRoot: Boolean = false,
)

/**
 * Specify a node customization parameter within a @DesignComponent function.
 *
 * @param node the name of the Figma node
 */
// TODO adding AnnotationTarget.PROPERTY to the list in @Target doesn't work. The annotation will
// not be found when iterating through annotations of the property due to a KSP bug:
// https://github.com/google/ksp/issues/1812. Once this is fixed, add AnnotationTarget.PROPERTY to
// this list so that @Design can be used in @DesignModuleClass class properties instead of
// @DesignProperty
@Target(AnnotationTarget.VALUE_PARAMETER) annotation class Design(val node: String)

/**
 * Specify a node customization property within a @DesignModule class.
 *
 * @param node the name of the Figma node
 */
// TODO remove this once the KSP bug is fixed. This is only because when @Design annotations for
// class properties cannot be found due to a bug in KSP.
@Target(AnnotationTarget.PROPERTY) annotation class DesignProperty(val node: String)

/**
 * Specify a variant name for a component set that contains variant children.
 *
 * @param property the name of the variant property
 */
// TODO add AnnotationTarget.PROPERTY to the list of targets when the KSP bug is fixed
@Target(AnnotationTarget.VALUE_PARAMETER) annotation class DesignVariant(val property: String)

/**
 * Specify a variant name for a component set that contains variant children. This annotation should
 * be used for @DesignModule class properties only
 *
 * @param property the name of the variant property
 */
// TODO remove this once the KSP bug is fixed. This is only needed because @DesignVariant
// annotations for class properties cannot be found due to a bug in KSP
@Target(AnnotationTarget.PROPERTY) annotation class DesignVariantProperty(val property: String)

/**
 * An optional annotation that goes with a @Design annotation of type @Composable() -> Unit, which
 * is used to replace the children of this frame with new data. Adding the @DesignContentTypes
 * annotation tells Design Compose what nodes can be used as children. This data is used in the
 * generated json file which is input for the DesignCompose Figma plugin.
 *
 * @param nodes A comma delimited string of node names that can go into the associated content
 *   replacement annotation
 */
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DesignContentTypes(val nodes: Array<String>)

@Target(AnnotationTarget.PROPERTY)
annotation class DesignContentTypesProperty(val nodes: Array<String>)

annotation class PreviewNode(val count: Int, val node: String)

/**
 * An optional annotation that goes with a @Design annotation of type @Composable() -> Unit, which
 * is used to provide sample content for the List Preview Widget. This data is used in the generated
 * json file which is input for the List Preview Widget.
 *
 * @param nodes A comma delimited string of node names that will be used as sample content
 */
@Repeatable
@Target(AnnotationTarget.VALUE_PARAMETER)
annotation class DesignPreviewContent(val name: String, val nodes: Array<PreviewNode>)

@Repeatable
@Target(AnnotationTarget.PROPERTY)
annotation class DesignPreviewContentProperty(val name: String, val nodes: Array<PreviewNode>)

/**
 * Meta keys that can be used with a character key to form a DesignKeyAction. For example, a list of
 * two DesignMetaKeys MetaCtrl and MetaAlt with the character 'C' represents a key event of the
 * letter 'C' with the control and alt keys held down.
 */
enum class DesignMetaKey {
    MetaShift,
    MetaCtrl,
    MetaMeta,
    MetaAlt,
}

/**
 * Generate a function that, when called, injects a key event with the given key and list of meta
 * keys.
 *
 * @param key the key to inject
 * @param metaKeys the list of meta keys held down when the key inject event occurs
 */
@Target(AnnotationTarget.FUNCTION)
annotation class DesignKeyAction(val key: Char, val metaKeys: Array<DesignMetaKey>)

/**
 * Generates a customizations() extension function for the class specified for this annotation. This
 * function returns a CustomizationContext that contains customizations for all the DesignCompose
 * annotated properties of the class. This class can be used in other classes and interfaces that
 * want to reuse these customizations without declaring them again.
 */
@Target(AnnotationTarget.CLASS) annotation class DesignModuleClass

/**
 * Add a module object as a parameter to a @DesignComponent function. All customizations within the
 * module object will be added to the function.
 */
// TODO add the annotation target PROPERTY to this once the KSP bug is fixed.
@Target(AnnotationTarget.VALUE_PARAMETER) annotation class DesignModule

/**
 * Add a module object as a property of another @DesignModuleClass object. All customizations within
 * the module object will be added to the containing class's customizations.
 */
// TODO remove this once the KSP bug is fixed. This is only because when @DesignModule annotations
//  for class properties cannot be found due to a bug in KSP.
@Target(AnnotationTarget.PROPERTY) annotation class DesignModuleProperty
