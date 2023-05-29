// 引入katex
import renderMathInElement from 'katex/contrib/auto-render/auto-render'
import 'katex/dist/katex.min.css'
// 引入katex
export default function renderLatex(element) {
  // 处理元素中的干扰标签
  const reg = /(\$\$[\s\S]*?\$\$)/g
  element.innerHTML = element.innerHTML.replace(reg, (match, p1) => {
    return p1.replace(/<br>/g, '')
  })
  // 渲染latex语法
  renderMathInElement(element, {
    delimiters: [
      { left: '$$', right: '$$', display: true },
      { left: '$', right: '$', display: false }
    ],
    ignoredTags: ['script', 'noscript', 'style', 'textarea', 'pre', 'code', 'a', 'img'],
    macros: {
      '\\ge': '\\geqslant',
      '\\le': '\\leqslant',
      '\\geq': '\\geqslant',
      '\\leq': '\\leqslant'
    }
  })
}
