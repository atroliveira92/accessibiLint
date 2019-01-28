package br.arthur.accessibilitylint;

import com.android.tools.lint.client.api.UElementHandler;
import com.android.tools.lint.detector.api.Context;
import com.android.tools.lint.detector.api.Detector;
import com.android.tools.lint.detector.api.Detector.UastScanner;
import com.android.tools.lint.detector.api.Implementation;
import com.android.tools.lint.detector.api.Issue;
import com.android.tools.lint.detector.api.JavaContext;
import com.android.tools.lint.detector.api.Scope;
import com.android.tools.lint.detector.api.UastLintUtils;
import com.intellij.psi.JavaElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.PsiType;

import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.uast.UAnnotation;
import org.jetbrains.uast.UBlockExpression;
import org.jetbrains.uast.UCallExpression;
import org.jetbrains.uast.UClass;
import org.jetbrains.uast.UDeclaration;
import org.jetbrains.uast.UDeclarationsExpression;
import org.jetbrains.uast.UElement;
import org.jetbrains.uast.UExpression;
import org.jetbrains.uast.UIfExpression;
import org.jetbrains.uast.ULiteralExpression;
import org.jetbrains.uast.ULocalVariable;
import org.jetbrains.uast.UMethod;
import org.jetbrains.uast.UParameter;
import org.jetbrains.uast.UTypeReferenceExpression;
import org.jetbrains.uast.UVariable;
import org.jetbrains.uast.java.JavaConstructorUCallExpression;
import org.jetbrains.uast.java.JavaUAssignmentExpression;
import org.jetbrains.uast.java.JavaUDeclarationsExpression;
import org.jetbrains.uast.java.JavaULocalVariable;
import org.jetbrains.uast.visitor.AbstractUastNonRecursiveVisitor;
import org.jetbrains.uast.visitor.AbstractUastVisitor;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.HashMap;
import java.util.List;

import static com.android.tools.lint.detector.api.Category.A11Y;
import static com.android.tools.lint.detector.api.Scope.JAVA_FILE_SCOPE;
import static com.android.tools.lint.detector.api.Severity.WARNING;

/**
 * Created by arthu on 21/01/2019.
 *
 */

public class RedirectOutContextDetector extends Detector implements Detector.UastScanner {

    static final Issue ISSUE_REDIRECT_OUT_OF_CONTEXT = Issue.create(
            "RedirectOuOfContext",
            "Avoid make the background view clickable",
            "Evitar a exigência de realizar toques no plano de fundo do aplicativo para realizar determinada ação, ou seja, fora dos componentes de interface",
            A11Y,
            6,
            WARNING,
            new Implementation(RedirectOutContextDetector.class, JAVA_FILE_SCOPE));


    @Override
    public List<Class<? extends UElement>> getApplicableUastTypes() {
        //return Collections.singletonList(ULiteralExpression.class);
        return Arrays.asList(UClass.class);
    }


    @Override
    public void visitMethod(JavaContext context, UCallExpression node, PsiMethod method) {
        super.visitMethod(context, node, method);
    }

    //    @Nullable
//    @Override
//    public List<String> getApplicableCallNames() {
//        return Collections.singletonList("startActivity");
//    }
//
//    @Nullable
//    @Override
//    public List<String> getApplicableMethodNames() {
//        return Collections.singletonList("startActivity");
//    }
//
//    @Nullable
//    @Override
//    public Collection<String> getApplicableElements() {
//        return Collections.singleton(INTENT);
//    }


    @Override
    public void afterCheckFile(Context context) {
        super.afterCheckFile(context);
        context.getConfiguration();
    }

    private HashMap<UCallExpression, UMethod > lstStartActivityExpressions = new HashMap<>();
    private List<String> valueParameter = new ArrayList<>();

    @Override
    public UElementHandler createUastHandler(JavaContext context) {
        // Not: Visiting UAST nodes is a pretty general purpose mechanism;
        // Lint has specialized support to do common things like "visit every class
        // that extends a given super class or implements a given interface", and
        // "visit every call site that calls a method by a given name" etc.
        // Take a careful look at UastScanner and the various existing lint check
        // implementations before doing things the "hard way".
        // Also be aware of context.getJavaEvaluator() which provides a lot of
        // utility functionality.

        return new UElementHandler() {


            private void check() {
                if(lstStartActivityExpressions.size() > 0 && valueParameter.size() > 0) {
                    for(UCallExpression uCallExpression : lstStartActivityExpressions.keySet()) {
                        UMethod method = lstStartActivityExpressions.get(uCallExpression);

                        if(method.getName().equalsIgnoreCase("onClick")) { // verify if the onClick method is from an Dialog
                            List<UParameter> params = method.getUastParameters();
                            if(params != null && params.size() > 0) {
                                UTypeReferenceExpression e = params.get(0).getTypeReference();
                                if(e != null && "DialogInterface".equalsIgnoreCase(e.toString())) {
                                    continue;
                                }
                            }
                        }

                        List<UExpression> uExpressions = uCallExpression.getValueArguments();
                        for (UExpression uExpression : uExpressions) {
                            String value = uExpression.toString();
                            if (valueParameter.contains(value)) {

                                context.report(ISSUE_REDIRECT_OUT_OF_CONTEXT, uCallExpression, context.getLocation(uCallExpression),
                                        "If this intent is opening out of context, please use a AlertDialog to ask to user if agree with the redirection");
                                break;
                            }
                        }
                    }
                    lstStartActivityExpressions.clear();
                    valueParameter.clear();
                }
            }

            @Override
            public void visitClass(UClass clazz){
                
                UMethod[] methods = clazz.getMethods();

                for(UMethod method : methods) {
                    UBlockExpression uastBody = (UBlockExpression) method.getUastBody();

                    if(uastBody != null) {
                        for (UExpression uExpression : uastBody.getExpressions()) {
                            if(uExpression instanceof JavaUAssignmentExpression) {
                                JavaUAssignmentExpression assignmentExpression = (JavaUAssignmentExpression) uExpression;
                                UExpression expression = assignmentExpression.getRightOperand();
                                if(expression.toString().contains("Intent.ACTION_VIEW")) {
                                    String value = assignmentExpression.getLeftOperand().toString();
                                    if(!TextUtils.isEmpty(value)) {
                                        if(value.contains(".")) {
                                            int index = value.indexOf(".");
                                            value = value.substring(index+1, value.length());
                                        }
                                        valueParameter.add(value);
                                    }
                                }

                            }
//                            else if(uExpression instanceof JavaUDeclarationsExpression) {
//                                uExpression.accept(new AbstractUastVisitor() {
//                                    @Override
//                                    public boolean visitLocalVariable(ULocalVariable node) {
//                                        if(node.getUastInitializer().toString().endsWith("(Intent.ACTION_VIEW)")) {
//                                            valueParameter.add(node.getName());
//                                        }
//                                        return super.visitLocalVariable(node);
//                                    }
//                                });
////                              JavaUDeclarationsExpression uDeclarationsExpression = (JavaUDeclarationsExpression) uExpression;
////                              if(uDeclarationsExpression.getDeclarations().size() > 0) {
////                                  if(uDeclarationsExpression.getDeclarations().get(0) instanceof JavaULocalVariable) {
////                                      JavaULocalVariable variable = (JavaULocalVariable) uDeclarationsExpression.getDeclarations().get(0);
////                                      if(variable.toString().endsWith("(Intent.ACTION_VIEW)")) {
////                                          valueParameter.add(variable.getName());
////                                      }
////                                  }
////                              }
//                            }
                            else if(uExpression instanceof UCallExpression) {
                                UCallExpression uCallExpression = (UCallExpression) uExpression;
                                if(uCallExpression.getMethodName() != null &&
                                        uCallExpression.getMethodName().equalsIgnoreCase("startActivity")) {
                                    lstStartActivityExpressions.put(uCallExpression, method);

                                }
                            }
                        }

                        //check();
                    }
                }

                check();
            }
        };
    }
}
