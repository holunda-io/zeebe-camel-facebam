// Generated on 2013-10-18 using generator-angular 0.5.0
'use strict';


module.exports = function(grunt) {
  require('load-grunt-tasks')(grunt);
  require('time-grunt')(grunt);

  grunt.initConfig({
    yeoman: {
      // configurable paths
      app: 'src',
      dist: 'dist'
    },
    watch: {
      sass: {
          files: ['<%= yeoman.app %>/styles/{,*/}*.{scss,sass}'],
          tasks: ['sass', 'autoprefixer']
      },
      styles: {
        files: ['<%= yeoman.app %>/styles/{,*/}*.css'],
        tasks: ['copy:styles', 'autoprefixer']
      },
      livereload: {
        options: {
          livereload: '<%= connect.options.livereload %>'
        },
        files: [
          '<%= yeoman.app %>/{,*/}*.html',
          '<%= yeoman.app %>/slides/*.md',
          '.tmp/styles/{,*/}*.css',
          '{.tmp,<%= yeoman.app %>}/scripts/{,*/}*.js',
          '<%= yeoman.app %>/images/{,*/}*.{png,jpg,jpeg,gif,webp,svg}'
        ]
      }
    },
    autoprefixer: {
      options: ['last 1 version'],
      dist: {
        files: [{
          expand: true,
          cwd: '.tmp/styles/',
          src: '{,*/}*.css',
          dest: '.tmp/styles/'
        }]
      }
    },
    connect: {
      options: {
        port: 9000,
        // Change this to '0.0.0.0' to access the server from outside.
        hostname: 'localhost',
        livereload: 35729
      },
      livereload: {
        options: {
          open: true,
          base: [
            '.tmp',
            '<%= yeoman.app %>',
            '.'
          ]
        }
      },
      dist: {
        options: {
          base: '<%= yeoman.dist %>'
        }
      }
    },
    clean: {
      dist: {
        files: [{
          dot: true,
          src: [
            '.tmp',
            '<%= yeoman.dist %>/*',
            '!<%= yeoman.dist %>/.git*'
          ]
        }]
      },
      server: '.tmp'
    },
    jshint: {
      options: {
        jshintrc: '.jshintrc'
      },
      all: [
        'Gruntfile.js',
        '<%= yeoman.app %>/scripts/{,*/}*.js'
      ]
    },
    sass: {
        dist: {
            options: {
                // sourceMap: true,
                style: 'expanded',
                includePaths: [
                    'node_modules/compass-mixins/lib/',
                    'node_modules/revealjs/css/'
                ]
            },
            files: [{
                expand: true,
                cwd: '<%= yeoman.app %>/styles',
                src: ['**/*.scss'],
                dest: '.tmp/styles',
                ext: '.css'
            }]
        }

    },
    concat: {
      '<%= yeoman.dist %>/scripts/reveal.classList.js': [
        'node_modules/reveal.js/lib/js/classList.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.marked.js': [
        'node_modules/reveal.js/plugin/markdown/marked.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.markdown.js': [
        'node_modules/reveal.js/plugin/markdown/markdown.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.highlight.js': [
        'node_modules/reveal.js/plugin/highlight/highlight.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.zoom.js': [
        'node_modules/reveal.js/plugin/zoom-js/zoom.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.notes.js': [
        'node_modules/reveal.js/plugin/notes/notes.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.search.js': [
        'node_modules/reveal.js/plugin/search/search.js'
      ],
      '<%= yeoman.dist %>/scripts/reveal.remotes.js': [
        'node_modules/reveal.js/plugin/remotes/remotes.js'
      ]
    },
    useminPrepare: {
      html: '<%= yeoman.app %>/index.html',
      options: {
        dest: '<%= yeoman.dist %>',
        flow: {
          steps: {
            'js': ['concat'],
            'css': ['concat']
          },
          post: {}
        }
      }
    },
    usemin: {
      html: ['<%= yeoman.dist %>/{,*/}*.html'],
      css: ['<%= yeoman.dist %>/styles/{,*/}*.css'],
      options: {
        dirs: ['<%= yeoman.dist %>', '<%= yeoman.dist %>/scripts']
      }
    },
    "regex-replace": {
      html: {
        src: ['<%= yeoman.dist %>/index.html','<%= yeoman.dist %>/scripts/reveal.notes.html'],
        actions: [{
          search: 'node_modules/reveal.js/lib/js/classList.js',
          replace: 'scripts/reveal.classList.js'
        }, {
          search: 'node_modules/reveal.js/plugin/markdown/marked.js',
          replace: 'scripts/reveal.marked.js'
        }, {
          search: 'node_modules/reveal.js/plugin/markdown/markdown.js',
          replace: 'scripts/reveal.markdown.js'
        }, {
          search: 'node_modules/reveal.js/plugin/highlight/highlight.js',
          replace: 'scripts/reveal.highlight.js'
        }, {
          search: 'node_modules/reveal.js/plugin/zoom-js/zoom.js',
          replace: 'scripts/reveal.zoom.js'
        }, {
          search: 'node_modules/reveal.js/plugin/notes/notes.js',
          replace: 'scripts/reveal.notes.js'
        }, {
          search: 'node_modules/reveal.js/plugin/search/search.js',
          replace: 'scripts/reveal.search.js'
        }, {
          search: 'node_modules/reveal.js/plugin/remotes/remotes.js',
          replace: 'scripts/reveal.remotes.js'
        },{
          search: '../../plugin/markdown/marked.js',
          replace: 'reveal.marked.js'
        }, {
          name: 'Inline Markdown files',
          search: '<section data-markdown="([^"]+)"(.*?)></section>',
          replace: function(wholeMatch, filename, attributes) {
            return '<section data-markdown ' + attributes + '><script type="text/template">' + grunt.file.read('src/' + filename) + '</script></section>';
            },
          flags: 'g'
          }]
      },
      removeSpeakerNotes : {
        src: ['<%= yeoman.dist %>/index.html'],
        actions: [
          {
            name: 'Set showNotes: false',
            search: 'showNotes: true',
            replace: 'showNotes: false'
          }, {
            //flags: 'gi',
            name: 'Remove Speaker Notes',
            search: /\n>NOTES:[\s\S]+?(---?)/gi,
            replace: function(wholeMatch, slideBreak) { return '\n'+slideBreak; }
          }, {
            name: 'Disable notes plugin',
            search: /^.+scripts\/reveal\.notes\.js.+$/gim,
            replace: ''
          }]
      }
    },
    imagemin: {
      dist: {
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>/images',
          src: '{,*/}*.{png,jpg,jpeg}',
          dest: '<%= yeoman.dist %>/images'
        }]
      }
    },
    htmlmin: {
      dist: {
        options: {
          /*removeCommentsFromCDATA: true,
          // https://github.com/yeoman/grunt-usemin/issues/44
          //collapseWhitespace: true,
          collapseBooleanAttributes: true,
          removeAttributeQuotes: true,
          removeRedundantAttributes: true,
          useShortDoctype: true,
          removeEmptyAttributes: true,
          removeOptionalTags: true*/
        },
        files: [{
          expand: true,
          cwd: '<%= yeoman.app %>',
          src: ['*.html'],
          dest: '<%= yeoman.dist %>'
        }]
      }
    },
    // Put files not handled in other tasks here
    copy: {
      dist: {
        files: [{
          expand: true,
          dot: true,
          cwd: '<%= yeoman.app %>',
          dest: '<%= yeoman.dist %>/',
          src: [
            '*.{ico,png,txt}',
            '.htaccess',
            'code/**',
            'samples/**',
            'images/{,*/}*.{gif,webp,svg}',
            'styles/fonts/*',
            '*.js'
          ]
        }, {
          expand: true,
          dest: '<%= yeoman.dist %>',
          src: [
            '<%= yeoman.app %>/'
          ]
        }, {
          expand: true,
          cwd: 'node_modules/reveal.js/plugin/notes',
          dest: '<%= yeoman.dist %>/scripts/',
          src: [
            'notes.html'
          ],
          rename: function(dest, src) {
            return dest + 'reveal.notes.html';
          }
        }, {
          expand: true,
          cwd: '.tmp/images',
          dest: '<%= yeoman.dist %>/images',
          src: [
            'generated/*'
          ]
        }]
      },
      styles: {
        expand: true,
        cwd: '<%= yeoman.app %>/styles',
        dest: '.tmp/styles/',
        src: '{,*/}*.css'
      }
    },
    concurrent: {
      server: [
        'sass',
        'copy:styles'
      ],
      dist: [
        'sass',
        'copy:styles',
        'imagemin',
        'htmlmin'
      ]
    }
  });

  grunt.registerTask('server', function(target) {
    if (target === 'dist') {
      return grunt.task.run(['build', 'connect:dist:keepalive']);
    }

    grunt.task.run([
      'clean:server',
      'concurrent:server',
      'autoprefixer',
      'connect:livereload',
      'watch'
    ]);
  });


  grunt.registerTask('build', [
    'clean:dist',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'copy:dist',
    'usemin',
    'regex-replace:html'
  ]);

  grunt.registerTask('release', [
    'clean:dist',
    'useminPrepare',
    'concurrent:dist',
    'autoprefixer',
    'concat',
    'copy:dist',
    'usemin',
    'regex-replace:html',
    'regex-replace:removeSpeakerNotes'
  ]);

  grunt.registerTask('default', [
    'server'
  ]);
};
