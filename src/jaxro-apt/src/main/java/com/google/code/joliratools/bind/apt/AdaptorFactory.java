/**
 * (C) 2009 jolira (http://www.jolira.com). Licensed under the GNU General
 * Public License, Version 3.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.gnu.org/licenses/gpl-3.0-standalone.html Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 */
package com.google.code.joliratools.bind.apt;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeMirror;

import com.google.code.joliratools.bind.model.Annotation;
import com.google.code.joliratools.bind.model.Class;
import com.google.code.joliratools.bind.model.Method;

interface AdaptorFactory {
    Annotation getAdapter(AnnotationMirror a);

    Class getAdapter(Element e);

    Method getAdapter(ExecutableElement e);

    Class getAdapter(TypeMirror m);
}
