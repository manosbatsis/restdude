/**
 * calipso-hub-framework - A full stack, high level framework for lazy application hackers.
 * Copyright © 2005 Manos Batsis (manosbatsis gmail)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.restdude.mdd.util;

import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.annotation.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class JavassistUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(JavassistUtil.class);
			
    public static String getGenericSignature(ClassPool pool, Class<?> targetType, Collection<Class<?>> impolementedTnterfaces, Collection<Class<?>> genericArguments) throws NotFoundException {
    	StringBuilder name = new StringBuilder( targetType.getSimpleName() );
    	StringBuilder sig = new StringBuilder("Ljava/lang/Object;L")
    		.append((targetType.getName().replace(".", "/")))
    		.append("<");
        if(genericArguments != null){
            for(Class<?> c  : genericArguments){
                pool.appendClassPath( new ClassClassPath(c));
                CtClass ct = pool.get( c.getName() );
                sig.append("L")
	                .append( ct.getName().replace('.', '/') )
	                .append(";");

                // while we're at it, append the class types to the name
                name.append("_").append(c.getSimpleName());
            }
        }
        sig.append(">;");
        return sig.toString();
    }

    public static Class<?> createInterface(String name, Class<?> superInterface, Collection<Class<?>> typeArgs) throws NotFoundException, CannotCompileException {
    	return createInterface(name, superInterface, typeArgs, false);
    }

    public static Class<?> createInterface(String name, Class<?> superInterface, Collection<Class<?>> typeArgs, boolean auditable) throws NotFoundException, CannotCompileException {
        ClassPool pool = ClassPool.getDefault();
        
        // add classpaths
        // pool.insertClassPath( new ClassClassPath(superInterface));
        for(Class<?> c : typeArgs){
            pool.insertClassPath( new ClassClassPath(c));
        }
        
        CtClass impl = pool.makeInterface( name );
        impl.setSuperclass( pool.get(superInterface.getName()) );
        impl.setGenericSignature( getGenericSignature(pool, superInterface, new ArrayList<Class<?>>(), typeArgs));
        
        Class<?> result = impl.toClass();
        return result;
    }
    
    protected static void addTypeAnnotations(CtClass clazz, Map<Class<?>, Map<String, Object>> typeAnnotations) {
		ClassFile ccFile = clazz.getClassFile();
		AnnotationsAttribute attr = getAnnotationsAttribute(ccFile);
		for(Class<?> annotationClass : typeAnnotations.keySet()){
			attr.addAnnotation(
				getTypeAnnotation(ccFile, ccFile.getConstPool(), annotationClass, typeAnnotations.get(annotationClass)));
		}
	}

    protected static Annotation getTypeAnnotation(ClassFile ccFile, ConstPool constPool, Class<?> annotationClass, Map<String, Object> members) {
    	Annotation annot = new Annotation(annotationClass.getName(), constPool);
		if(MapUtils.isNotEmpty(members)){
			for(String valueName : members.keySet()) {
				Object value = members.get(valueName);
				if (valueName != null && value != null) {
					MemberValue memberValue = createMemberValue(constPool, value);
					annot.addMemberValue(valueName, memberValue);
				}
			}
		}
		return annot;
	}

    protected static AnnotationsAttribute getAnnotationsAttribute(ClassFile ccFile) {
		AnnotationsAttribute attr = (AnnotationsAttribute) ccFile.getAttribute(AnnotationsAttribute.visibleTag);
		if (attr == null) {
			attr = new AnnotationsAttribute(ccFile.getConstPool(), AnnotationsAttribute.visibleTag);
			ccFile.addAttribute(attr);
		}
		return attr;
	}
	

	
    protected static MemberValue createMemberValue(ConstPool constPool, Object val) {
		MemberValue memberVal = null;

		if (val instanceof Boolean) {
			memberVal = new BooleanMemberValue((Boolean)val, constPool);
		}
		else if (val instanceof String) {
			memberVal = new StringMemberValue(val.toString(), constPool); 
		}
		else if (val instanceof String[]) {
			String[] sVal = (String[]) val;
			StringMemberValue[] stringMemberValue = new StringMemberValue[sVal.length];
			for(int i =0; i < sVal.length; i++){
				stringMemberValue[i] = new StringMemberValue(sVal[i], constPool); 
			}
			memberVal = new ArrayMemberValue(constPool);
			((ArrayMemberValue) memberVal).setValue(stringMemberValue);
		}
		else if (val instanceof Class) {
			memberVal = new ClassMemberValue(((Class<?>)val).getName(), constPool);
		}
		else if (val instanceof Byte) {
			memberVal = new ByteMemberValue((Byte)val, constPool);
		}
		else if (val instanceof Character) {
			memberVal = new CharMemberValue((Byte)val, constPool);
		}
		else if (val instanceof Double) {
			memberVal = new DoubleMemberValue((Double)val, constPool);
		}
		else if (val instanceof Float) {
			memberVal = new FloatMemberValue((Float)val, constPool);
		}
		else if (val instanceof Integer) {
			memberVal = new IntegerMemberValue((Integer)val, constPool);
		}
		else if (val instanceof Short) {
			memberVal = new ShortMemberValue((Short)val, constPool);
		}
		else if (val instanceof Long) {
			memberVal = new LongMemberValue((Long)val, constPool);
		}
		else if (val instanceof Enum[]) {
			Enum[] sVal = (Enum[]) val;
			EnumMemberValue[] enumMemberValue = new EnumMemberValue[sVal.length];
			for(int i =0; i < sVal.length; i++){
				enumMemberValue[i] = new EnumMemberValue(constPool); 
				enumMemberValue[i].setType(sVal[i].getClass().getName());
				enumMemberValue[i].setValue(sVal[i].toString());
			}
			memberVal = new ArrayMemberValue(constPool);
			((ArrayMemberValue) memberVal).setValue(enumMemberValue);
		}
		else if (val instanceof Enum) {
			memberVal = new EnumMemberValue(constPool);
			((EnumMemberValue)memberVal).setType(val.getClass().getName());
			((EnumMemberValue)memberVal).setValue(((Enum<?>)val).toString());
		}
		return memberVal;
	}
	
    public static Class<?> createClass(CreateClassCommand command) {
        try{
            ClassPool pool = ClassPool.getDefault();
            pool.appendClassPath(new ClassClassPath(command.getBaseImpl()));

            // Generate our actual base class
            CtClass ctBaseImpl = pool.get(command.getBaseImpl().getName());
            CtClass impl = pool.makeClass( command.getName() );
            impl.setSuperclass(ctBaseImpl);
            // add interfaces
            if(!CollectionUtils.isEmpty(command.getInterfaces())){
                for( Class<?> iface : command.getInterfaces()){
                    impl.addInterface(pool.get(iface.getName()));
                }
            }
            // add type annotations
            if(MapUtils.isNotEmpty(command.getTypeAnnotations())){
                addTypeAnnotations(impl, command.getTypeAnnotations());
            }
            
            // apply our generic signature
            impl.setGenericSignature( getGenericSignature(pool, command.getBaseImpl(), command.getInterfaces(), command.getGenericTypes()) );

            // add constructors
            CtConstructor[] constructors = ctBaseImpl.getConstructors();
            if( constructors == null ||  constructors.length == 0){
                // Add a default constructor
                CtConstructor constructor = CtNewConstructor.defaultConstructor(impl);
                constructor.setBody("{}");
                impl.addConstructor(constructor);
            }else{
                for(CtConstructor c : constructors){
                    CtNewConstructor.copy(c, ctBaseImpl, null);
                }
            }

            CtMethod m = CtNewMethod.make(
                    "public java.lang.reflect.TypeVariable[] getTypeParameters() { throw new RuntimeException(); }",
                    impl);
            impl.addMethod(m);


            Class<?> result = impl.toClass();

            return result;
        }catch(Exception e){
            throw new RuntimeException("Failed to create subclass: " + e.getMessage(), e);
        }
    }
    


}