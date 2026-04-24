import Cookies from 'js-cookie'

/**
 * Token 的 Cookie 键名
 */
const TokenKey = 'Digital_Park_Token'

/**
 * 获取 Token
 * @returns {string|undefined}
 */
export function getToken() {
  return Cookies.get(TokenKey)
}

/**
 * 设置 Token
 * @param {string} token - Token 值
 * @param {boolean} remember - 是否记住登录（7天有效期）
 */
export function setToken(token, remember = false) {
  if (remember) {
    // 记住登录，设置7天有效期
    return Cookies.set(TokenKey, token, { expires: 7 })
  }
  // 默认会话级别
  return Cookies.set(TokenKey, token)
}

/**
 * 移除 Token
 */
export function removeToken() {
  return Cookies.remove(TokenKey)
}
