file = ARGV[0]
if((not file.include?("source")) and (not file.include?("pom")) and file.include?(".jar"))
  basename = File.basename(file)
  noext = File.basename(file, '.jar')
  dir = File.dirname(file)
  version = File.basename(dir)
  parent = File.dirname(dir)
  source = parent + "/source/" + version + "/source-" + version + ".jar"
  lnsource = "../source/" + version + "/source-" + version + ".jar"
  new_source = dir + "/" + noext + "-sources.jar"
  if(File.exist?(source))
    system("ln -fs #{lnsource} #{new_source}")
  end
end
#./team/cvs/ui/source/3.3.100/source-3.3.100.jar
#./team/cvs/ui/3.3.100/ui-3.3.100.jar
