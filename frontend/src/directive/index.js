/**
 * 自定义指令
 * v-hasPermi - 权限校验指令
 * 使用方式：v-hasPermi="['system:user:add']"
 */

import store from '@/store'

/**
 * 检查用户是否拥有指定权限
 * @param {Array} permissions - 权限标识数组
 * @returns {boolean}
 */
function checkPermi(permissions) {
  const userStore = store.useUserStore()
  const allPermission = '*:*:*'
  const userPermissions = userStore.permissions || []

  if (permissions && permissions.length > 0) {
    const permissionFlag = permissions
    return userPermissions.some((permission) => {
      return (
        allPermission === permission ||
        permissionFlag.includes(permission)
      )
    })
  }
  return false
}

export default {
  install(app) {
    /**
     * v-hasPermi 权限指令
     * 用法：v-hasPermi="['system:user:add']"
     * 如果用户没有对应权限，则移除该 DOM 元素
     */
    app.directive('hasPermi', {
      mounted(el, binding) {
        const { value } = binding
        if (value && Array.isArray(value) && value.length > 0) {
          const hasPermission = checkPermi(value)
          if (!hasPermission) {
            // 没有权限，移除元素
            el.parentNode && el.parentNode.removeChild(el)
          }
        } else {
          throw new Error('需要指定权限标识，如 v-hasPermi="[\'system:user:add\']"')
        }
      }
    })

    /**
     * v-hasRole 角色指令
     * 用法：v-hasRole="['admin']"
     * 如果用户没有对应角色，则移除该 DOM 元素
     */
    app.directive('hasRole', {
      mounted(el, binding) {
        const { value } = binding
        const userStore = store.useUserStore()
        const superAdmin = 'admin'
        const roles = userStore.roles || []

        if (value && Array.isArray(value) && value.length > 0) {
          const hasRole = roles.some((role) => {
            return superAdmin === role || value.includes(role)
          })
          if (!hasRole) {
            el.parentNode && el.parentNode.removeChild(el)
          }
        } else {
          throw new Error('需要指定角色标识，如 v-hasRole="[\'admin\']"')
        }
      }
    })
  }
}
