/**
 * Appends a line to block of string content.
 * @param {string} content
 * @param {string} value
 */
function appendLine (content, value) {
  var lines = content.toString().split('\n')

  lines.push(value)

  return lines.join('\n')
}

/**
 * Gets the index of a line of text containing a specific value.
 * @param {string} content
 * @param {string} value
 * @returns {number}
 */
function getLineIndex (content, value) {
  const lines = content.toString().split('\n')

  for (let i = 0, length = lines.length; i < length; i++) {
    if (lines[i].search(value) === -1) {
      continue
    }

    return i
  }

  return -1
}

/**
 * Inserts a line of text at a specific index location.
 * @param {string} content
 * @param {number} index
 * @param {string} value
 * @returns {string}
 */
function insertLineAt (content, index, value) {
  var lines = content.toString().split('\n')

  lines.splice(index, 0, value)

  return lines.join('\n')
}

module.exports = {
  appendLine,
  getLineIndex,
  insertLineAt
}
